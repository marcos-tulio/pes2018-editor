package com.github.marcos.tulio.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MrCapybara
 */
public class ReaderText {

    private InputStream _inputStream;
    private InputStreamReader _inputStreamReader;
    private BufferedReader _bufferedReader;

    public ReaderText() {
    }

    public ReaderText(InputStream inputStream) {
        createReader(inputStream);
    }

    public final void createReader(InputStream inputStream) {
        _inputStream = inputStream;
        _inputStreamReader = new InputStreamReader(inputStream);
        _bufferedReader = new BufferedReader(_inputStreamReader);
    }

    public void closeReader() throws IOException {
        _inputStream.close();
        _inputStreamReader.close();
        _bufferedReader.close();
    }

    public String readToString() throws IOException {
        String line, output = "";
        while ((line = _bufferedReader.readLine()) != null) {
            output += line;
        }

        closeReader();
        return output;
    }

    public List<String> readToList() throws IOException {
        List<String> list = new ArrayList<>();
        String line;

        while ((line = _bufferedReader.readLine()) != null) {
            if ((!"".equals(line)) && (!line.isEmpty())) {
                list.add(line);
            }
        }
        
        closeReader();
        return list;
    }

    public BufferedReader getReader() {
        return _bufferedReader;
    }
}
