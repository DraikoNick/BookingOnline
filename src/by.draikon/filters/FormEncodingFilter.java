package by.draikon.filters;

import by.draikon.model.utils.Constants;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter (
        urlPatterns = {Constants.URL_REGIN},
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8")
        }
)
public class FormEncodingFilter implements Filter {
    private static final String FILTERABLE_CONTENT_TYPE="application/x-www-form-urlencoded";
    private static final String ENCODING_DEFAULT = "UTF-8";
    private static final String ENCODING_INIT_PARAM_NAME = "encoding";
    private String encoding;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String contentType = req.getContentType();
        if (contentType != null && contentType.startsWith(FILTERABLE_CONTENT_TYPE)){
            req.setCharacterEncoding(encoding);
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter(ENCODING_INIT_PARAM_NAME);
        if (encoding == null){
            encoding = ENCODING_DEFAULT;
        }
    }
}
