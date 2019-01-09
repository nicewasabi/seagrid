package com.webyun.seagrid.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * @Project: seaGrid
 * @Title: GraphUtil
 * @Description: 图形绘制工具类
 * @author: songwj
 * @date: 2017-9-22 下午4:27:25
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class GraphUtil {
	
	/**
	 * @Description: 按指定角度绘制箭头
	 * @author: songwj
	 * @date: 2017-9-22 下午4:27:39
	 * @param angle 度数（0~360）
	 * @param out 图像输出路径
	 * @return
	 */
	public static boolean drawArrowByAngle(double angle, OutputStream out) {
		boolean result = false;
		
		try {
			BufferedImage sourceImage = new BufferedImage(20, 20, BufferedImage.TYPE_4BYTE_ABGR);
			// 绘制箭头
			Graphics2D g = (Graphics2D) sourceImage.getGraphics();
			int[] xPoints = { 9, 11, 11, 18, 10, 2, 9 };
			int[] yPoints = { 2, 2, 14, 10, 18, 10, 14 };
			g.setColor(Color.BLACK);
			int len = xPoints.length;
			g.drawPolygon(xPoints, yPoints, len);
			g.fillPolygon(xPoints, yPoints, len);
			// 获取原图宽高
			int w = sourceImage.getWidth();
			int h = sourceImage.getHeight();
			
			// 存放旋转后的图像
			BufferedImage rotatedImage = new BufferedImage(w, h, sourceImage.getType());
			// 使用矩阵旋转
			AffineTransform at = new AffineTransform();
			// 将角度转为弧度
			double rad = Math.toRadians(angle);
			at.rotate(rad, w / 2, h / 2);// 旋转图象
			at.translate(0, 0);// 原点坐标
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
			op.filter(sourceImage, rotatedImage);
			
			// 最终出图
			BufferedImage finalImg = new BufferedImage(w, h, sourceImage.getType());
			g = finalImg.createGraphics();
			g.fillRect(0, 0, w, h);
			g.drawImage(rotatedImage, 0, 0, w, h, null);
			
			// 释放资源
			g.dispose();
			
			result = ImageIO.write(finalImg, "PNG", out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * @Description: 根据数据值及角度绘制图标
	 * @author: songwj
	 * @date: 2017-9-22 下午4:28:07
	 * @param value 数据值
	 * @param angle 角度（0~360）
	 * @param out 图像输出路径
	 * @return
	 */
	public static boolean drawArrowByAngleAndValue(double value, double angle, OutputStream out) {
		boolean result = false;
		
		try {
			int bkgdWidth = 30;
			int bkgdHeight = 30;
			BufferedImage sourceImage = new BufferedImage(bkgdWidth, bkgdHeight, BufferedImage.TYPE_4BYTE_ABGR);
			// 绘制箭头
			Graphics2D g = (Graphics2D) sourceImage.getGraphics();
			int windLeverLen;// 风向杆长度
			int iniLen = 11;// 风向杆初始长度
			int lenUnit = 3;// 长度增量
			if (value < 1) {
				windLeverLen = iniLen;
			} else if (1 <= value && value <2) {
				windLeverLen = iniLen + lenUnit;
			} else if (2 <= value && value < 3) {
				windLeverLen = iniLen + 2 * lenUnit;
			} else if (3 <= value && value < 5) {
				windLeverLen = iniLen + 3 * lenUnit;
			} else if (5 <= value && value < 8) {
				windLeverLen = iniLen + 4 * lenUnit;
			} else {
				windLeverLen = iniLen + 5 * lenUnit;
			}
			// 左上角xy值
			int x = bkgdWidth / 2 - 1;
			int y = (bkgdHeight - windLeverLen - 3) / 2;
			int[] xPoints = { x, x + 2, x + 2, x + 8, x + 1, x - 6, x };
			// 保证图标上下居中显示
			int[] yPoints = { y, y, y + windLeverLen, y + windLeverLen - 4, y + windLeverLen + 3, y + windLeverLen - 4, y + windLeverLen };
			g.setColor(Color.BLACK);
			int len = xPoints.length;
			g.drawPolygon(xPoints, yPoints, len);
			g.fillPolygon(xPoints, yPoints, len);
			
			// 存放旋转后的图像
			BufferedImage rotatedImage = new BufferedImage(bkgdWidth, bkgdHeight, sourceImage.getType());
			// 使用矩阵旋转
			AffineTransform at = new AffineTransform();
			// 将角度转为弧度
			double rad = Math.toRadians(angle);
			at.rotate(rad, bkgdWidth / 2, bkgdHeight / 2);// 旋转图象
			at.translate(0, 0);// 原点坐标
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
			op.filter(sourceImage, rotatedImage);
			
			// 最终出图
			BufferedImage finalImg = new BufferedImage(bkgdWidth, bkgdHeight, sourceImage.getType());
			g = finalImg.createGraphics();
			g.drawImage(rotatedImage, 0, 0, bkgdWidth, bkgdHeight, null);
			
			// 释放资源
			g.dispose();
			
			result = ImageIO.write(finalImg, "PNG", out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
