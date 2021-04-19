package cspfilter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.portlet.RenderResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;

public class BufferedServletResponseWrapper extends ServletResponseWrapper {

    public BufferedServletResponseWrapper(ServletResponse response) {
        super(response);

        charWriter = new CharArrayWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (getWriterCalled) {
            throw new IllegalStateException("getWriter already called");
        }

        getOutputStreamCalled = true;

        return super.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }

        if (getOutputStreamCalled) {
            throw new IllegalStateException("getOutputStream already called");
        }

        getWriterCalled = true;

        writer = new PrintWriter(charWriter);

        return writer;
    }

    public String toString() {
        String s = null;

        if (writer != null) {
            s = charWriter.toString();
        }

        return s;
    }

    protected CharArrayWriter charWriter;
    protected PrintWriter writer;
    protected boolean getOutputStreamCalled;
    protected boolean getWriterCalled;

}
