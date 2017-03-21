package com.opp.util;

import com.opp.exception.InternalServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Created by jhermida on 10/11/16.
 */
public class JavaRunCommand {

    public static Optional<String> run(String command)  {
        Optional<String> results = Optional.empty();
        Process process;
        try {
            process = Runtime.getRuntime().exec(new String[]{
                    "bash",
                    "-c",
                    command
            });

            if (process != null) {
                process.waitFor();
                try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    results = Optional.ofNullable(br.readLine());
                } catch (IOException e){
                    throw new InternalServiceException("Unable to export " + command, e);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new InternalServiceException("Unable to communicate with Soasta with command " + command, e);
        }

        return results;
    }
}
