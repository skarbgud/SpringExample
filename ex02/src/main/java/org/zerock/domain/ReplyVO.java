package org.zerock.domain;

import java.util.Date;
import lombok.Data;

@Data
public class ReplyVO {
/*
   create table tbl_reply(
    rno number(10,0),
    bno number(1,10) not null,
    reply varchar2(1000) not null,
    replyer varchar2(50) not null,
    replyDate date default sysdate,
    updateDate date default sysdate
);
 */
	private Long rno;
	private Long bno;
	
	private String reply;
	private String replyer;
	private Date replyDate;
	private Date updateDate;	
}
