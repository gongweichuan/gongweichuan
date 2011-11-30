/*
 * 创建日期 2006-9-25
 *
 */
package com.coolsql.pub.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

/**
 * @author liu_xlin
 *自定义边框类，提供凸出和下凹两种类型的边框
 */
public class ThreeDBorder extends AbstractBorder {
   public static final int RAISE=0; //凸出
   public static final int LOWER=1; //下凹
   
   private int type=0; //边框类型
   private final int thickness = 1;
   public ThreeDBorder(int type)
   {
   	 if(type<0||type>1)
   	 	throw new IllegalArgumentException("border type is not exists,type:"+type);
   	 this.type=type;
   }
   /**
    * 绘制边框
    */
   public void paintBorder(Component c, Graphics g, int x, int y,
                           int width, int height) {
       g.setColor(type==RAISE?Color.white:Color.gray);
       g.drawLine(0, 0, width - 1, 0);
       g.drawLine(0, 0, 0, height - 1);
       g.setColor(type==RAISE?Color.gray:Color.white);
       g.drawLine(width - 1, 0, width - 1, height);
       g.drawLine(0, height - 1, width, height - 1);
   }
   /**
    * 重写边框与组件的边距
    */
   public Insets getBorderInsets(Component c) {
       return new Insets(thickness, thickness, thickness, thickness);
   }
}
