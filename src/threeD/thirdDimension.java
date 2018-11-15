package threeD;

import java.awt.*;

public class thirdDimension {
	public static final int screenW = 800;
	public static final int screenH = 600;
	public static final int screenRes = 4;
	public static final double maxRender = 20;
	public static final double pHeight = 0.5;
	public static final double pSpeed = 0.3;
	public static final double hFOV = 180 * Math.PI / 180;
	public static final double vFOV = 180 * Math.PI / 180;
	public static double pDir = 180 * Math.PI / 180;
	public static double pxPos = 3;
	public static double pyPos = 5;
	
	public static final int[][] map =  {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
		{ 1, 0, 1, 1, 0, 0, 1, 0, 0, 1},
		{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
		{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
		{ 1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
		{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	public static void main(String[] args) throws InterruptedException {
		DrawingPanel panel = new DrawingPanel(800,600);
		Graphics g = panel.getGraphics();
		g.setColor(Color.BLACK);
		
		while(true) {
			g.setColor(Color.WHITE);
			g.fillRect(0,0,screenW,screenH);
			g.setColor(Color.BLACK);
			drawScreen(g);
			pDir += 0.1;
			pDir = pDir%(2*Math.PI);
			checkCollision();
			//pxPos += Math.cos(pDir) * pSpeed;
			//pyPos += Math.sin(pDir) * pSpeed;
			Thread.sleep(50);
		}
	}
	
	public static void drawScreen(Graphics g) {
		double theta;
		for (int i = 0; i < screenW / screenRes; i++) {
			theta = (pDir - hFOV / 2 + i * hFOV * screenRes / screenW);
			double dist = raycast(pxPos, pyPos, theta);
			//if (dist > 10) {
			//	dist = 0;
			//}
			//dist = 0;
			System.out.println(dist + " " + i + " " + theta + " " + (vAngle(dist, 0)) + " " + pDir);
			//g.drawOval(i*8, (int)Math.floor(dist) * 8 + 300, 4, 4);
			g.drawOval((int)(20 * dist * Math.cos(theta))+400, (int)(20 * dist * Math.sin(theta))+300, 8, 8);
			//g.drawOval(i * 8, 300, 4, 4);
			//g.drawLine((int)(i * screenRes), 300 + (int)(vAngle(dist, 0) * screenH), (int)(i * screenRes) + screenRes, (int)(vAngle(dist, 1) * screenH) + 300);
			g.fillRect((int)(i * screenRes), 300 + (int)(vAngle(dist, 0) * screenH), screenRes, (int)(vAngle(dist, 1) * screenH) - (int)(vAngle(dist, 0) * screenH));
		}
	}
	
	public static double raycast(double px, double py, double theta) {
		double gx;
		double gy;
		double dist = 0;
		if (0 < Math.cos(theta))  {
			gx = 1;
		} else if (0 > Math.cos(theta)) {
			gx = -1;
		} else {
			gx = px;
		}
		if (0 < Math.sin(theta))  {
			gy = 1;
		} else if (0 > Math.sin(theta)) {
			gy = 0;
		} else {
			gy = py;
		}
		boolean xax = gx != px;
		boolean yax = gy != py;
		
		double dx;
		double dy;
		double hx = 2;
		double hy = 2;
		double h;
		double npx;
		double npy;
		
		double test = Math.sin(theta);
		
		int gridx = (int)Math.floor(px);
		int gridy = (int)Math.floor(py);
		while (map[gridx][gridy] != 1 && dist < maxRender) {
			if (gx > 0) {
				dx = Math.floor(px + 1) - px;
			} else {
				dx = Math.ceil(px - 1) - px;
			}
			if (gy > 0) {
				dy = Math.floor(py + 1) - py;
			} else {
				dy = Math.ceil(py - 1) - py;
			}
			
			if (xax) {
				hx = dx / Math.cos(theta);
			}
			if (yax) {
				hy = dy / Math.sin(theta);
			}
			if (hx < hy) {
				h = hx;
			} else {
				h = hy;
			}
			npx = h * Math.cos(theta);
			npy = h * Math.sin(theta);
			dist += Math.sqrt(npx*npx + npy*npy);
			px += npx;
			py += npy;
			if (gx > 0) {
			gridx = (int)Math.floor(px - 0.000);
			} else {
				gridx = (int)Math.floor(px - 0.001);
			}
			if (gy > 0) {
				gridy = (int)Math.floor(py - 0.000);
			} else {
				gridy = (int)Math.floor(py - 0.001);
			}
			

			//System.out.println(dist + " " + px + " " + py + " " + hx + " " + hy + " " + h);
		}
		return dist;
	}
	
	public static double vAngle(double d, double h) {
		double hTheta = Math.atan((h - pHeight) / d);
		return hTheta / vFOV;
		
	}
	
	public static void checkCollision() {
		double pxNew = pxPos + pSpeed * Math.cos(pDir);
		double pyNew = pyPos + pSpeed * Math.sin(pDir);
		int gridx = (int)Math.floor(pxNew);
		int gridy = (int)Math.floor(pyNew);
		if (map[gridx][(int)pyPos] != 1) {
			pxPos = pxNew;
		}
		if (map[(int)pxPos][gridy] != 1) {
			pyPos = pyNew;
		}
	}
}
