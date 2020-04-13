package com.macaron.vra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WavUtil {

	private static final Logger logger = LoggerFactory.getLogger(WavUtil.class);

	private static void execCmd(String cmd) throws IOException, InterruptedException {
		logger.info("cmd:{}", cmd);

		List<String> cmdList = new ArrayList<String>();
//		cmdList.add("cmd.exe");
//		cmdList.add("/C");
		cmdList.add(cmd);
		 
		Process proc = Runtime.getRuntime().exec(String.join(" ", cmdList));
		//Process proc = new ProcessBuilder(cmdList).redirectErrorStream(true).start();

		InputStream stdOut = proc.getInputStream();
		BufferedReader brStdOut = new BufferedReader(new InputStreamReader(stdOut, "MS950"));
		String stdLine = null;
		logger.info("<wavCombineOutput>");
		while ((stdLine = brStdOut.readLine()) != null) {
			logger.info("{}", stdLine);
		}
		logger.info("</wavCombineOutput>");

		InputStream errOut = proc.getErrorStream();
		BufferedReader brErrOut = new BufferedReader(new InputStreamReader(errOut, "MS950"));
		String errLine = null;
		logger.info("<wavCombineOutput error>");
		while ((errLine = brErrOut.readLine()) != null) {
			logger.info("{}", errLine);
		}
		logger.info("</wavCombineOutput error>");

		int exitVal = proc.waitFor();
		logger.info("exitVal:{}", exitVal);
	}

	public static void convertAudioToWav(File srcFile, File destFile) {
		StringBuilder cmd = new StringBuilder();
		// convert to wav
		String osName = System.getProperty("os.name" );
		String ffmpeg = "ffmpeg";
		String quot = "'";
		if(osName.equalsIgnoreCase("Linux")) {
			ffmpeg ="./ffmpeg" ;
			quot=" ";
		}
//		cmd.append(ffmpeg).append(" -acodec adpcm_ima_oki -f s16le -ar 8000 -y -i ").append(quot).append(srcFile.toString()).append(quot).append(quot)
//				.append(destFile.toString()).append(quot);

		cmd.append(ffmpeg).append(" -acodec adpcm_ima_oki -f s16le -ar 8000 -y -i ")
		.append(quot).append(srcFile.toString()).append(quot)
		.append(" ")
		.append(quot).append(destFile.toString()).append(quot);
		
		try {
			execCmd(cmd.toString());
		} catch (IOException | InterruptedException e) {
			logger.error("convertAudioToWav error {}", e);
		}
	}

	public static void convertAllAudioToWav(Path cvtWavTmpPath, Path speechSubPath) throws IOException {
		Files.walk(cvtWavTmpPath).forEach(f -> {
		
			if (f.toFile().exists()) {
				String extension = FilenameUtils.getExtension(f.toString());
				if(!extension.equals("wav")) {
					File destFile = new File(speechSubPath.toString(), f.getFileName().toString().replace(extension, "wav"));
					convertAudioToWav(f.toFile(), destFile);	
				}else {
					try {
						Files.copy(f, new File(speechSubPath.toString(), f.getFileName().toString()).toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});

	}

	public static void main(String[] args) {

	}

}
