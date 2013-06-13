package com.andeveloper.kyodai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;

/**
 * 棋盘
 */
public class Chessboard extends Group {
	private static int[] NUM_ARRAY = { 2, 4, 6, 8 };
	private List<Chess> chessList = new ArrayList<Chess>();
	private Chess[][] chesses;
	public int col;
	private Action ft1;
	private Action ft2;
	private List<LinkLine> linesList = new ArrayList<LinkLine>();
	private boolean[][] map;
	private Stack<PathPoint> path = new Stack<PathPoint>();
	private Chess prevChess;
	private Random random = new Random(System.currentTimeMillis());
	private int remaining;
	public int row;
	private int tc;
	private int total;
	private int tr;

	public void initChessboard(int row, int column) {
		int i = row * column;
		if (i % 2 != 0)
			throw new RuntimeException("棋子总数不是双数");
		this.row = row;
		this.col = column;
		this.tr = (row + 2);
		this.tc = (column + 2);
		this.width = (this.tr * Chess.REGION_WIDTH);
		this.height = (this.tc * Chess.REGION_HEIGHT);
		this.x = (GameConfig.SCREEN_WIDTH / 2 - column * Chess.REGION_WIDTH / 2);
		this.y = (GameConfig.SCREEN_HEIGHT / 2 - row * Chess.REGION_HEIGHT / 2);
		if (this.ft1 != null) {
			this.ft1.reset();
			this.ft1 = null;
		}
		if (this.ft2 != null) {
			this.ft2.reset();
			this.ft2 = null;
		}
		this.total = i;
		this.remaining = i;
		super.clear();
		createChesses();
		updateTexture();
		resort();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		Actor actor = hit(x, y);
		if (actor != null && (actor instanceof Chess) && actor.visible
				&& !((Chess) actor).disappearing) {
			return true;
		}
		return super.touchDown(x, y, pointer);
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		super.touchUp(x, y, pointer);
		Actor actor = hit(x, y);
		if (actor != null && (actor instanceof Chess) && actor.visible
				&& !((Chess) actor).disappearing) {
			Chess curChess = (Chess) actor;
			if (prevChess == null) {
				prevChess = curChess;
				curChess.action(Forever.$(Sequence.$(FadeTo.$(0.5f, 0.1f),
						FadeTo.$(1.0f, 0.1f))));
			} else {
				if (prevChess != curChess) {
					if ((prevChess.getRegion().getTexture() != curChess
							.getRegion().getTexture())
							|| !findPath(prevChess, curChess)) {
						prevChess.clearActions();
						prevChess.color.a = 1.0f;
						prevChess = curChess;
						curChess.action(Forever.$(Sequence.$(
								FadeTo.$(0.5f, 0.1f), FadeTo.$(1.0f, 0.1f))));
					} else {// 找到可消除的棋子
						prevChess.clearActions();
						prevChess.color.a = 1.0f;

						final Group localGroup = createLinkLines();
						addActor(localGroup);

						ft1 = FadeTo.$(0.5f, 0.4f);
						ft2 = FadeTo.$(0.5f, 0.4f);
						ft1.setCompletionListener(new OnActionCompleted() {

							@Override
							public void completed(Action action) {
								action.getTarget().visible = false;
							}
						});
						ft2.setCompletionListener(new OnActionCompleted() {

							@Override
							public void completed(Action action) {
								action.getTarget().visible = false;
								remaining -= 2;
								removeActor(localGroup);
								localGroup.getActors();
								synchronized (Chessboard.this.linesList) {
									List<Actor> localLines = localGroup
											.getActors();
									int size = localLines.size();
									for (int i = 0; i < size; ++i) {
										Chessboard.this.linesList
												.add((LinkLine) localLines
														.get(i));
									}
								}
							}
						});

						prevChess.disappearing = true;
						curChess.disappearing = true;
						prevChess.action(ft1);
						curChess.action(ft2);
						prevChess = null;
						if (GameConfig.sound) {
							Assets.sel.play();
							Assets.elec.play();
						}
					}
				}
			}
		}
	}

	private Group createLinkLines() {
		Group lines = new Group();
		synchronized (linesList) {
			int m = path.size() * 2 - linesList.size();
			for (int i = 0; i < m; ++i) {
				linesList.add(new LinkLine(new TextureRegion(Assets.line)));
			}
			m = 0;
			while (!path.empty()) {
				PathPoint p = path.pop();
				p.row--;
				p.col--;
				LinkLine l = null;
				switch (p.predir) {
				case 1:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH;
					l.y = p.row * Chess.REGION_HEIGHT + Chess.REGION_HEIGHT / 2
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = 0;
					l.rotation = 0;
					break;
				case 2:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH + Chess.REGION_WIDTH / 2;
					l.y = p.row * Chess.REGION_HEIGHT + Chess.REGION_HEIGHT / 2
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = l.getTextureRegion().getRegionHeight() / 2;
					l.rotation = 90;
					break;
				case 3:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH + Chess.REGION_WIDTH / 2;
					l.y = p.row * Chess.REGION_HEIGHT + Chess.REGION_HEIGHT / 2
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = 0;
					l.rotation = 0;
					break;
				case 4:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH + Chess.REGION_WIDTH / 2;
					l.y = p.row * Chess.REGION_HEIGHT
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = l.getTextureRegion().getRegionHeight() / 2;
					l.rotation = 90;
					break;
				}
				if (l != null) {
					lines.addActor(l);
				}
				l = null;
				switch (p.dir) {
				case 1:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH + Chess.REGION_WIDTH / 2;
					l.y = p.row * Chess.REGION_HEIGHT + Chess.REGION_HEIGHT / 2
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = 0;
					l.rotation = 0;
					break;
				case 2:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH + Chess.REGION_WIDTH / 2;
					l.y = p.row * Chess.REGION_HEIGHT
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = l.getTextureRegion().getRegionHeight() / 2;
					l.rotation = 90;
					break;
				case 3:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH;
					l.y = p.row * Chess.REGION_HEIGHT + Chess.REGION_HEIGHT / 2
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = 0;
					l.rotation = 0;
					break;
				case 4:
					l = linesList.get(m++);
					l.x = p.col * Chess.REGION_WIDTH + Chess.REGION_WIDTH / 2;
					l.y = p.row * Chess.REGION_HEIGHT + Chess.REGION_HEIGHT / 2
							- l.getTextureRegion().getRegionHeight() / 2;
					l.originX = 0;
					l.originY = l.getTextureRegion().getRegionHeight() / 2;
					l.rotation = 90;
					break;
				}
				if (l != null) {
					lines.addActor(l);
				}
			}
			linesList.removeAll(lines.getActors());
		}
		return lines;
	}

	private void createChesses() {
		int i = this.total - this.chessList.size();
		if (i > 0) {
			this.chesses = ((Chess[][]) Array.newInstance(Chess.class,
					new int[] { this.tr, this.tc }));
			this.map = ((boolean[][]) Array.newInstance(Boolean.TYPE,
					new int[] { this.tr, this.tc }));
		} else {
			clearChesses();
			clearMap();
		}
		for (int j = 0; j < i; ++j) {
			this.chessList.add(new Chess());
		}
		int n = 0;
		for (int j = 1; j <= this.row; ++j) {
			for (int k = 1; k <= this.col; ++k) {
				this.chesses[j][k] = chessList.get(n++);
				this.chesses[j][k].clearActions();
				this.chesses[j][k].color.a = 1.0F;
				this.chesses[j][k].visible = true;
				this.chesses[j][k].disappearing = false;
				addActor(this.chesses[j][k]);
			}
		}
	}

	private void clearChesses() {
		for (int i = 0; i < chesses.length; ++i) {
			for (int j = 0; j < chesses[i].length; ++j) {
				chesses[i][j] = null;
			}
		}
	}

	private void clearMap() {
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[i].length; ++j) {
				map[i][j] = false;
			}
		}
	}

	private void updateTexture() {
		// 随机排序
		for (int i = 0; i < Assets.icons.length; ++i) {
			int j = this.random.nextInt(Assets.icons.length);
			int k = this.random.nextInt(Assets.icons.length);
			if (j != k) {
				Texture localTexture = Assets.icons[j];
				Assets.icons[j] = Assets.icons[k];
				Assets.icons[k] = localTexture;
			}
		}

		int m = 0;
		int n = -1;
		for (int i = 1; i <= this.row; ++i) {
			for (int j = 1; j <= this.col; ++j) {
				if (m == 0) {
					if (this.total / 2 > Assets.icons.length) {
						m = NUM_ARRAY[Math.max(1,
								this.random.nextInt(NUM_ARRAY.length))];
					} else {
						m = NUM_ARRAY[Math.min(NUM_ARRAY.length - 2,
								this.random.nextInt(NUM_ARRAY.length))];
					}
					n++;
				}
				TextureRegion localTextureRegion = new TextureRegion(
						Assets.icons[n], 2, 2, Chess.REGION_WIDTH,
						Chess.REGION_HEIGHT);
				this.chesses[i][j].setRegion(localTextureRegion);
				this.chesses[i][j].pack();
				--m;
			}
		}
	}

	/**
	 * 寻找路径，有可行的路返回true,否则返回false
	 * 
	 * @param chess1
	 * @param chess2
	 * @return
	 */
	private boolean findPath(Chess chess1, Chess chess2) {
		if (chess1 == null || chess2 == null) {
			return false;
		}
		if (chess1 == chess2) {
			return false;
		}
		int w = 0;
		boolean result = false;
		path.clear();
		path.push(new PathPoint(chess1.row, chess1.col, 0, 0));
		while (!path.empty()) {
			PathPoint curPoint = path.pop();

			if (curPoint.dir != 0 && !path.empty()
					&& curPoint.dir != path.peek().dir) {// 拐点回退，拐点减1
				--w;
			}
			map[curPoint.row][curPoint.col] = false;
			for (int i = curPoint.dir + 1; i <= 4; ++i) {// 遍历四个方向
				int row = curPoint.row;
				int col = curPoint.col;
				switch (i) {
				case 1:// 右
					col++;
					break;
				case 2:// 下
					row--;
					break;
				case 3:// 左
					col--;
					break;
				case 4:// 上
					row++;
					break;
				}
				if (row < 0 || row >= this.tr || col < 0 || col >= this.tc) {// 出界
					continue;
				}
				if (map[row][col]) {
					continue;
				}
				if (chesses[row][col] == null || !chesses[row][col].visible) {
					if (!path.empty()) {
						PathPoint prevPoint = path.peek();
						if (prevPoint.dir != i) {// 新拐点
							if (w + 1 > 2) {// 拐点不允许超过2个
								continue;
							}
							++w;
						}
					}
					curPoint.dir = i;
					path.push(curPoint);
					path.push(new PathPoint(row, col, 0, i));
					map[curPoint.row][curPoint.col] = true;
					break;
				} else if (chesses[row][col] == chess2) {// 找到一条可行路径
					if (!path.empty()) {
						PathPoint prevPoint = path.peek();
						if (prevPoint.dir != i) {// 新拐点
							if (w + 1 > 2) {// 拐点不允许超过2个
								continue;
							}
							++w;
						}
					}
					curPoint.dir = i;
					path.push(curPoint);
					path.push(new PathPoint(row, col, 0, i));
					map[curPoint.row][curPoint.col] = true;
					result = true;
					break;
				}
			}
			if (result)
				break;
		}
		clearMap();
		return result;
	}

	/**
	 * 重新排序棋子
	 */
	public void resort() {
		for (int i = 1; i <= row; ++i) {
			for (int j = 1; j <= col; ++j) {
				int n1 = 1 + random.nextInt(col);
				int n2 = 1 + random.nextInt(col);
				if (n1 != n2) {
					Chess tmp = chesses[i][n1];
					chesses[i][n1] = chesses[i][n2];
					chesses[i][n2] = tmp;
				}
			}
		}

		for (int i = 1; i <= col; ++i) {
			for (int j = 1; j <= row; ++j) {
				int n1 = 1 + random.nextInt(row);
				int n2 = 1 + random.nextInt(row);
				if (n1 != n2) {
					Chess tmp = chesses[n1][i];
					chesses[n1][i] = chesses[n2][i];
					chesses[n2][i] = tmp;
				}
			}
		}

		for (int i = 1; i <= row; ++i) {
			for (int j = 1; j <= col; ++j) {
				this.chesses[i][j].x = ((j - 1) * Chess.REGION_WIDTH);
				this.chesses[i][j].y = ((i - 1) * Chess.REGION_HEIGHT);
				this.chesses[i][j].row = i;
				this.chesses[i][j].col = j;
			}
		}
	}

	public int getRemaining() {
		return this.remaining;
	}

	public static class PathPoint {
		public int row;
		public int col;
		public int dir;
		public int predir;

		public PathPoint(int row, int col, int dir, int predir) {
			this.row = row;
			this.col = col;
			this.dir = dir;
			this.predir = predir;
		}
	}

}
