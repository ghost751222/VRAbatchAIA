package com.macaron.vra.util;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

public class FileSystemUtil {
	
	
	public static FileObject createFolder(String uri) throws FileSystemException{
		FileSystemManager fsm = VFS.getManager();
		
		FileObject fo = fsm.resolveFile(uri);
//		logger.info("{}", fo.exists());
		if(fo==null || !fo.exists()){
			fo.createFolder();
		}
		if(!fo.isFolder()){
			throw new RuntimeException("File is not a folder:" + uri);
		}
		
		return fo;
	}

	public static FileObject createFile(String uri) throws FileSystemException{
		FileSystemManager fsm = VFS.getManager();
		
		FileObject fo = fsm.resolveFile(uri);
		
		if(fo==null || !fo.exists()){
			fo.createFile();
		}
		if(!fo.isFile()){
			throw new RuntimeException("File is not a file:" + uri);
		}
		
		return fo;
	}
}
