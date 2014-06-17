//package br.ce.ufc.compNuvem;
//
//import java.io.IOException;
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.*;
//
//@WebFilter("/*")
//public class LoginFilter implements Filter {
//
//public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//  String path = ((HttpServletRequest) request).getRequestURI();
//  System.out.println(path);
//
//  HttpSession session = ((HttpServletRequest)request).getSession(false);
//  if (path.toLowerCase().contains("login") || path.toLowerCase().contains("javax.faces.resource")) {
//    chain.doFilter(request, response);
//  } else if (session != null && session.getAttribute("nome") != null) {
//    chain.doFilter(request, response);
//  } else {
//	String contextPath = ((HttpServletRequest) request).getContextPath();
//    ((HttpServletResponse)response).sendRedirect(contextPath+"/login.xhtml");
//  }
//}
//
//public void destroy() {}
//
//public void init(FilterConfig arg0) throws ServletException {}
//}
