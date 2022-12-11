CREATE TABLE `aibees`.`file_metadata` (
  `fileid` VARCHAR(10) NOT NULL,
  `filename` VARCHAR(45) NOT NULL,
  `path` VARCHAR(100) NOT NULL,
  `desc` VARCHAR(300) NULL,
  `createtime` DATETIME NULL,
  PRIMARY KEY (`fileid`),
  UNIQUE INDEX `fileid_UNIQUE` (`fileid` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;
