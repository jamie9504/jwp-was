package jwp.was.webapplicationserver.configure.controller;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jwp.was.webapplicationserver.configure.controller.handlebar.RowNumberHelper;
import jwp.was.webserver.HttpStatusCode;
import jwp.was.webserver.dto.HttpRequest;
import jwp.was.webserver.dto.HttpResponse;

public class ModelAndView {

    private static final String NOT_FOUND_PAGE_MESSAGE = "페이지를 찾을 수 없습니다.";
    private static final String TEXT_HTML = "text/html";
    private static final String PREFIX_HANDLEBAR_FILE = "/webapp";
    private static final String SUFFIX_HANDLEBAR_FILE = ".html";

    private final Object model;
    private final String view;

    public ModelAndView(Object model, String view) {
        this.model = model;
        this.view = view;
    }

    public HttpResponse toHttpResponse(HttpRequest httpRequest) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(CONTENT_TYPE, TEXT_HTML);
            return HttpResponse
                .of(httpRequest.getHttpVersion(), HttpStatusCode.OK, headers, makeBody());
        } catch (IOException e) {
            return HttpResponse
                .of(httpRequest.getHttpVersion(), HttpStatusCode.NOT_FOUND, NOT_FOUND_PAGE_MESSAGE);
        }
    }

    private String makeBody() throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(PREFIX_HANDLEBAR_FILE);
        loader.setSuffix(SUFFIX_HANDLEBAR_FILE);
        Handlebars handlebars = new Handlebars(loader);
        handlebars.registerHelper("rowNumber", new RowNumberHelper());
        Template template = handlebars.compile(view);
        return template.apply(model);
    }
}
