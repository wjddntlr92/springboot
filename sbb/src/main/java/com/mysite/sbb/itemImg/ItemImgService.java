package com.mysite.sbb.itemImg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.sbb.item.SiteItem;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class ItemImgService {
	
	private final ItemImgRepository itemImgRepository;
	
	@Value("${file.download.directory}")
	private String uploadRootDirectory;
	
	
	public boolean imgFileCheck(List<MultipartFile> imgs) {
		final String[] PERMISSION_FILE_EXT_ARR = {"GIF", "JPEG", "JPG", "PNG", "BMP", "PDF", "MP4"};
	
		boolean imgBoolean=false;
		for( MultipartFile img : imgs) {
			imgBoolean = false;
			String originFileName =  img.getOriginalFilename();
			String originFileExtension = originFileName.substring(originFileName.lastIndexOf(".")+1).toUpperCase();
			
			for(String ext :PERMISSION_FILE_EXT_ARR) {	
				
				System.out.println("이미지파일다름");
				if(ext.equals(originFileExtension) ) {
					
					imgBoolean =true;
					
					break;
					
				}
				
			}
			if(imgBoolean==false) {
				break;
			}
			
		}
		return imgBoolean;
	}
	
	
	public ResponseEntity<?> createImg(List<MultipartFile> imgs, SiteItem siteItem) throws  IOException {
		
		int i=1;
		for( MultipartFile img : imgs) {
			
			ItemImg itemImg = new ItemImg();
			String originFileName = img.getOriginalFilename();
			String originFileExtension = originFileName.substring(originFileName.lastIndexOf(".")).toUpperCase();
			
			
			itemImg.setSiteItem(siteItem);
			itemImg.setItemImgOriginName(originFileName);
			itemImg.setCreateDate(LocalDateTime.now());
			itemImg.setMainImg(1);
			String fileName = siteItem.getAuthor().getUsername()+"_"+siteItem.getId()+"_"+i+originFileExtension;
			URL r = this.getClass().getResource("");
			String path = uploadRootDirectory+fileName;		
			
			try {
				img.transferTo(new File(path));
			}catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			
			itemImg.setItemImgUploadName(fileName);
			itemImg.setItemImgUploadPath(path);
			
			this.itemImgRepository.save(itemImg);
			i++;
			
		}
		return ResponseEntity.ok("file upload ok~!");
	}

}