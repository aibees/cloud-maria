package com.aibees.service.maria.multipart.domain.repo;

import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.entity.FileImage;
import com.aibees.service.maria.multipart.domain.entity.QFileImage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileImageRepository extends JpaRepository<FileImage, Long>, FileImageRepoCustom {

    List<FileImage> findByCategory(String category);
    List<FileImage> findByCategoryAndYm(String category, String ym);
}

interface FileImageRepoCustom {
    List<FileImage> selectDisplayImageList(FileImageReq param);
    Long getFileImageMaxId();
}

@AllArgsConstructor
class FileImageRepoCustomImpl implements FileImageRepoCustom {

    private final JPAQueryFactory query;
    private static final QFileImage qFileImage = QFileImage.fileImage;

    @Override
    public List<FileImage> selectDisplayImageList(FileImageReq param) {
        return query.selectFrom(qFileImage)
            .where(createWhereClause(param))
            .orderBy(qFileImage.createTime.desc())
            .limit(param.getSearchSize())
            .fetch();
    }

    @Override
    public Long getFileImageMaxId() {
        return query.select(qFileImage.id.max())
                .from(qFileImage)
                .fetchOne();
    }

    private BooleanBuilder createWhereClause(FileImageReq param) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if(param.getCreateAt() != null) {
            whereClause.and(qFileImage.createTime.lt(param.getCreateAt()));
            whereClause.and(qFileImage.displayYn.eq(("Y")));
        }

        return whereClause;
    }
}