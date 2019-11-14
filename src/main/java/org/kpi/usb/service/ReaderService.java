package org.kpi.usb.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReaderService {

    public List<String> readFile(String uri) {
        List<String> lines = new ArrayList<>();

        try {
             lines = Files.readAllLines(Paths.get(uri),
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
