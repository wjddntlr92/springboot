package com.mysite.sbb.item;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.sbb.itemImg.ItemImgService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/item")
@Controller
public class ItemController {
	
	private final UserService userService;
	private final ItemService itemService;
	private final ItemImgService itemImgService;
	
	
	@GetMapping("/list")
	public String itemList() {
		

		
		return "/item/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/upload")
	public String uploadItem(ItemUploadForm itemUploadForm) {
		return "/item/upload";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/upload")
	public String uploadItem(@Valid ItemUploadForm itemUploadForm, BindingResult bindingResult, @RequestParam("imgs") List<MultipartFile> imgs, Principal principal) throws IOException {
		if(bindingResult.hasErrors()) {
			return "/item/upload";
		}
		
		if(imgs.size()<2) {			
			bindingResult.reject("이미지이상", "이미지는 2개이상 넣어주세요");
			return "/item/upload";
		}
		
		if(this.itemImgService.imgFileCheck(imgs)==false) {
			bindingResult.reject("이미지이상", "이미지 파일만 첨부 가능합니다.");
			return "/item/upload";
		}
		
		
		
		SiteUser siteUser = this.userService.getUser(principal.getName());
		SiteItem siteItem = this.itemService.createItem(itemUploadForm.getItemName(),itemUploadForm.getItemDetail(),siteUser, itemUploadForm.getPrice(),itemUploadForm.getAmount());
		
		this.itemImgService.createImg(imgs, siteItem);
		
		return "redirect:/item/list";
	}
	
//	@PostMapping("/upload")
//    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
//        String fileName = file.getOriginalFilename();
//        Path dest = Paths.get("/");
//
//        try {
//            file.transferTo(dest);
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok("file upload ok~!");
//    }
//

}
