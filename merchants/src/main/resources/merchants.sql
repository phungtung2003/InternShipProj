CREATE TABLE t_merchant (
    id int(10) unsigned NOT NULL AUTO_INCREMENT,
    name varchar(64) COLLATE utf8_bin NOT NULL,
    logo_url varchar(256) COLLATE utf8_bin NOT NULL,
    business_license_url varchar(256) COLLATE utf8_bin NOT NULL,
    phone varchar(64) COLLATE utf8_bin NOT NULL,
    address varchar(64) COLLATE utf8_bin NOT NULL,
    is_audit BOOLEAN NOT NULL COMMENT 'verify if business license ok',
    UNIQUE (id),
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;


-- unsigned表示恒为正数
-- collate utf8_bin是 以二进制值比较，也就是区分大小写，collate是核对的意思
-- uft-8_general_ci  一般比较，不区分大小写
-- AUTO_INCREMENT=17表示id的自增从17开始