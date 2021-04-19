package cspfilter;

import java.io.*;
import javax.servlet.http.*;

public class CharResponseWrapper extends HttpServletResponseWrapper {
    private CharArrayWriter writer;

    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        writer = new CharArrayWriter();
    }

    public PrintWriter getWriter() {
        return new PrintWriter(writer);
    }

    public String toString() {
        return writer.toString();
    }

}