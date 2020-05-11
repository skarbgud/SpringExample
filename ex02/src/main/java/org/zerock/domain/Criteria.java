package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	private int pageNum; //페이지 번호
	private int amount;	//한 페이지당 몇 개의 데이터를 보여줄 것인지
	
	private String type;
	private String keyword;

	
	public Criteria() {	//생성자를 통해서 기본값을 1페이지, 10개로 지정해서 처리
		this(1,10);
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public String[] getTypeArr() {			//검색조건이 각 글자(T,W,C)로 구성되어 있으므로 검색 조건을 배열로 만들어 한번에 처리하기 위함
		 return type == null? new String[] {}: type.split("");
	}
	
	public String getListLink() {
	UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
			.queryParam("pageNum", this.pageNum)
			.queryParam("amount", this.getAmount())
			.queryParam("type", this.getType())
			.queryParam("keyword", this.getKeyword());
	
		return builder.toUriString();
	}
	
}