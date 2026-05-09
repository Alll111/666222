package com.controller;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.annotation.IgnoreAuth;
import com.entity.EIException;
import com.utils.R;

@RestController
@RequestMapping("/upload")
public class UploadController {

	private File getAvatarDirectory() {
		File upload = new File(System.getProperty("user.dir"), "upload" + File.separator + "avatar").getAbsoluteFile();
		if (!upload.exists()) {
			upload.mkdirs();
		}
		return upload;
	}

	@IgnoreAuth
	@PostMapping("/avatar")
	public R uploadAvatar(MultipartFile file, HttpServletRequest request) throws Exception {
		if (file == null || file.isEmpty()) {
			throw new EIException("上传文件不能为空");
		}

		String originalFileName = file.getOriginalFilename();
		String suffix = ".jpg";
		if (originalFileName != null && originalFileName.lastIndexOf('.') >= 0) {
			suffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
		}

		String fileName = new Date().getTime() + suffix;
		File dest = new File(getAvatarDirectory(), fileName);
		file.transferTo(dest);

		String relativePath = "/upload/avatar/" + fileName;
		return R.ok()
				.put("file", relativePath)
				.put("url", relativePath)
				.put("fileName", fileName);
	}
}
