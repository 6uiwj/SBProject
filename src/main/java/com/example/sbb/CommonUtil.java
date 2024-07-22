package com.example.sbb;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

//마크다운 컴포넌트
@Component //스프링 부트가 관리하는 빈으로 등록
public class CommonUtil {

    /**
     * markdown 메서드 - 마크다운 텍스트를 HTML 문서로 변환하여 리턴
     * @param markdown
     * @return
     */
    public String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}

