package com.mysite.sbb.item;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;

import com.mysite.sbb.itemImg.ItemImg;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private SiteUser author;
	
	@Column
	private String itemName;
	
	
	private String itemDetail;
	
	
	private int price;
	
	
	private int amount;
	
	@OneToMany(mappedBy="siteItem",cascade=CascadeType.REMOVE)
	private List<ItemImg> itemImgs; 
	
	@CreatedDate
    private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;
	
	@Enumerated(EnumType.ORDINAL)
	private ItemStatus itemStatus; 
	

}
