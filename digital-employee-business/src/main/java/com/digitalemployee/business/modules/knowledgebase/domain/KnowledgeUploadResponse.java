package com.digitalemployee.business.modules.knowledgebase.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeUploadResponse extends ChatRemoteResponse{

    private List<String> ids;

}
