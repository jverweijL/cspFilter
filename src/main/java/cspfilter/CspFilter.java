package cspfilter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import org.osgi.service.component.annotations.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jverweij
 */
@Component(
		immediate = true,
		property = {
				"service.ranking:Integer=9999",
				"servlet-context-name=",
				"servlet-filter-name=CSP Filter",
				"url-pattern=/web/*"
		},
		service = Filter.class
)
public class CspFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Called filter init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("Called CSP doFilter");

		_log.info("CSP Filter is invoked");
		System.out.println("CSP IS HERE TO STAY");

		CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) response);

		chain.doFilter(request, wrapper);

		PrintWriter responseWriter = response.getWriter();

		if (wrapper.getContentType().contains("text/html")) {
			CharArrayWriter charWriter = new CharArrayWriter();
			String originalContent = wrapper.toString().replaceAll("<script ","<script nonce='CSP_NONCE' ");
					originalContent = originalContent.replaceAll("<style","<style nonce='CSP_NONCE' ");
					originalContent = originalContent.replaceAll("eval","//CSP EVAL");
			//System.out.println("F: " + originalContent);

			//originalContent = originalContent.replaceAll("script", "script CSP");

			int indexOfCloseBodyTag = originalContent.indexOf("</body>") - 1;

			charWriter.write(originalContent.substring(0, indexOfCloseBodyTag));

			String copyrightInfo = "<p>CSP NONCE EVAL</p>";
			charWriter.write(copyrightInfo);

			String closeHTMLTags = "</body></html>";
			charWriter.write(closeHTMLTags);

			String alteredContent = charWriter.toString();
			response.setContentLength(alteredContent.length());
			responseWriter.write(alteredContent);
		}
	}

	@Override
	public void destroy() {
		System.out.println("Called filter destroy");
	}

	private static final Log _log = LogFactoryUtil.getLog(CspFilter.class);
}