package com.bugbycode.webapp.controller.login;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.util.ImgCodeUtil;

@Controller
public class LoginController {
	
	@RequestMapping("/login")
	public String login() {
		return "pages/login";
	}
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/login";
	}
	
	@RequestMapping("/imgCode")
	public void imgCode(HttpServletResponse response,HttpSession session) throws IOException {
		String codeKey = "abcdefghkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
		OutputStream os=response.getOutputStream();
		Random random = new Random();
		int width=160, height=42;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		ImgCodeUtil imgCodeUtil = new ImgCodeUtil();
		
		Graphics g = imgCodeUtil.getImgCode(image.getGraphics(), random, width, height);
		
		char[] arr = codeKey.toCharArray();
		
		String sRand="";
		
		StringBuffer buff = new StringBuffer();
		
		for (int j=0;j<6;j++){
			
			int code = (int)(Math.random() * 53);
			
			char c = arr[code];
			
			String rand = String.valueOf(c);
			
			g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
			
			g.drawString(rand,25*j+6,30);
			
			buff.append(rand);
		}
		
		sRand = buff.toString();
		
		session.setAttribute("rand",sRand.toUpperCase());
		
		g.dispose();
		
		ImageIO.write(image, "JPEG",os);
		
		os.flush();
		os.close();
		os=null;
		
		response.flushBuffer();
	}
}
