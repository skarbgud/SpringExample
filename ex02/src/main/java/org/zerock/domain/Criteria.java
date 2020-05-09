package org.zerock.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	private int pageNum; //페이지 번호
	private int amount;	//한 페이지당 몇 개의 데이터를 보여줄 것인지
	
	public Criteria() {	//생성자를 통해서 기본값을 1페이지, 10개로 지정해서 처리
		this(1,10);
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
}
