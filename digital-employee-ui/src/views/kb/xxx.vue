<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      v-show="showSearch"
      :inline="true"
    >
      <el-form-item label="文档名称" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文档名称"
          clearable
          maxlength="200"
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文档来源" prop="userId" v-if="userType != '02'">
        <el-select
          v-model="queryParams.userId"
          placeholder="请选择文档来源"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in subList"
            :key="dict.userId"
            :label="dict.userName"
            :value="dict.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生成类型" prop="userId">
        <el-select
          v-model="queryParams.graphType"
          placeholder="请选择生成类型"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in graphTypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="文档目录" prop="directoryId" class="kb_cascader">
        <el-cascader
          :options="docOptions"
          v-model="docValArr"
          popper-class="kb_cascader_radio"
          clearable
          expand-trigger="hover"
          placeholder="请选择文档目录"
          :props="{ value: 'id', label: 'directory', children: 'children',expandTrigger: 'hover', checkStrictly: true }"
          :show-all-levels="false"
          :key="refreshItem"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="文档标签" prop="labelId" class="kb_cascader">
        <el-cascader
          :options="labelOptions"
          v-model="labelValArr"
          clearable
          expand-trigger="hover"
          placeholder="请选择文档标签"
          popper-class="kb_cascader_radio"
          :props="{ value: 'id', label: 'label', children: 'children', expandTrigger: 'hover', checkStrictly: true }"
          :show-all-levels="false"
          :key="refreshItem"
        ></el-cascader>
      </el-form-item>

      <el-form-item label="文档类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择文档类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in relationOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="城市区域" prop="areaId" class="kb_cascader">
        <el-cascader
          :options="areaArr"
          :show-all-levels="false"
          v-model="searchArea"
          clearable
          expand-trigger="hover"
          :props="optionProps"
          popper-class="kb_cascader_radio"
          @change="changeArea"
          :key="refreshItem"
          placeholder="请选择城市区域"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="模板名称" prop="templateId" >
        <el-select v-model="queryParams.templateId" placeholder="请选择模板" clearable size="small">
          <el-option
            v-for="dict in tempOptions"
            :key="dict.id"
            :label="dict.fileName"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年限范围">
        <el-date-picker
          v-model="queryParams.startDate"
          size="small"
          style="width:113px!important"
          clearable
          type="year"
          :editable="false"
          :default-value="defaultValue"
          format="yyyy"
          value-format="yyyy"
          placeholder="开始年"
        ></el-date-picker>
        -
        <el-date-picker
          v-model="queryParams.endDate"
          size="small"
          style="width:113px!important"
          clearable
          format="yyyy"
          :editable="false"
          :default-value="defaultValue"
          value-format="yyyy"
          type="year"
          placeholder="结束年"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="导入时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          clearable
          :editable="false"
          value-format="yyyy-MM-dd"
          type="daterange"
          placeholder="导入时间"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          v-if="!open"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >导入</el-button
        >
        <el-button
          type="primary"
          v-else
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >导入</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-dropdown>
          <el-button type="primary" size="mini">
            下载帮助<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              @click.native="downloadRelation(item)"
              v-for="item in relationData"
              :key="item.key"
              >{{ item.key }}</el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <!-- <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar> -->
    </el-row>
    <div class="gs_list">
      <el-table v-loading="loading" :data="kbList" height="100%" class="kb_form_warp">
        <el-table-column label="序号" type="index" width="80" align="center">
          <template slot-scope="scope">
            {{
              scope.$index + 1 + (queryParams.pageNum - 1) * queryParams.pageSize
            }}
          </template>
        </el-table-column>
        <el-table-column label="文档名称" prop="fileName" align="center" min-width="150" >
          <template slot-scope="scope">
            <title-tip :titleName="scope.row.fileName" :width="200" :nameNumber="10"></title-tip>
          </template>
        </el-table-column>
        <el-table-column label="文档来源" width="120" align="center" >
            <template slot-scope="scope">
              {{ filterUser(scope.row) }}
            </template>
        </el-table-column>
        <el-table-column label="生成类型" prop="graphType" align="center" width="150">
          <template slot-scope="scope">
            {{ scope.row.graphType === 0 ? "智能构建" : "普通构建" }}
          </template>
        </el-table-column>
        <el-table-column label="文档目录" prop="directory" align="center" min-width="150" >
          <template slot-scope="scope">
            <title-tip :titleName="scope.row.directory" :width="200" :nameNumber="10"></title-tip>
          </template>
        </el-table-column>
        <el-table-column label="文档标签" prop="label" align="center" min-width="150">
          <template slot-scope="scope">
            <title-tip :titleName="scope.row.label" :width="200" :nameNumber="10"></title-tip>
          </template>
          <!-- <template slot-scope="scope">
            <span v-if="scope.row.label == null || scope.row.label.length<10">{{scope.row.label}}</span>
            <span v-else>{{scope.row.label.substring(0,6) + '...' }}</span>
            <el-button type="text" @click="info(scope.row)" v-if="scope.row.label == null ||scope.row.label.length>10">详情</el-button>
          </template> -->
        </el-table-column>
        <el-table-column label="文档类型" min-width="100" align="center" >
          <template slot-scope="scope">
            {{ filterType(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="模板名称" prop="templateName" align="center" min-width="150" :show-overflow-tooltip="true"/>
        <el-table-column label="导入时间" prop="createTime" align="center" min-width="150">
          <template slot-scope="scope">
            <span> {{scope.row.createTime}}</span>
          </template>
        </el-table-column>
        <el-table-column label="图谱ID" prop="graphId" align="center" min-width="150" />
        <el-table-column
          label="操作"
          align="center"
          width="420"
          class-name="small-padding fixed-width">
         <template slot-scope="scope">
           <el-button
              size="mini"
              type="text"
              :icon="iconStatus(scope.row.status)"
              @click="changeStatusKB(scope.row)"
              :disabled="scope.row.status != 0"
              />
<!--              <img v-if="scope.row.status == 2 || scope.row.status == 3" src="../../assets/images/222.gif" alt="" style="width:14px;height: 14px">-->
<!--              {{ scope.row.status | getStatus }}</el-button-->
<!--            >-->
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              style="margin-left: 20px"
              @click="handleDoc(scope.row)"
              :disabled="scope.row.status != 1"
              >文档</el-button
            >
            <el-dropdown trigger="click">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-s-unfold"
                style="margin-left: 20px"
                >更多操作</el-button
              >
              <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item @click.native="changeGraphId(scope.row)"
                v-if="scope.row.graphType === 1"
                >{{ scope.row.graphId ? "修改" : "添加" }}图谱ID</el-dropdown-item>
                <el-dropdown-item @click.native="downloadDoc(scope.row)"
                :disabled="scope.row.status != 1"
                v-if="scope.row.graphType === 1">图谱下载</el-dropdown-item>
                <!-- <el-dropdown-item @click.native="kbResult(scope.row)"
                  :disabled="scope.row.status != 1">结构化结果查看</el-dropdown-item> -->
                <el-dropdown-item @click.native="kbInfo(scope.row)">详情</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              style="margin-left: 20px"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
            </template>
          <!-- <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="changeStatusKB(scope.row)"
              :disabled="scope.row.graphType == 1 && scope.row.graphId == null"
              >
              <img v-if="scope.row.status == 2 || scope.row.status == 3" src="../../assets/images/222.gif" alt="" style="width:14px;height: 14px">
              {{ scope.row.status | getStatus }}</el-button
            >
            <span style="width: 70px;display: inline-block;">
              <el-button
                size="mini"
                type="text"
                @click="downloadDoc(scope.row)"
                :disabled="scope.row.status != 1"
                v-if="scope.row.graphType === 1"
              >图谱下载</el-button
              >
            </span>
            <el-button
              size="mini"
              type="text"
              @click="kbInfo(scope.row)"
              >详情</el-button
            >

            <span style="width: 70px;display: inline-block;">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="changeGraphId(scope.row)"
                v-if="scope.row.graphType === 1"
                >{{ scope.row.graphId ? "修改" : "添加" }}图谱ID</el-button
              >
            </span>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              style="margin-left: 20px"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template> -->
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="total > 0"
      :total="total"
      :pageSizes="pageSizes"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList(queryParams.pageSize)"
    />
    <el-dialog title="导入" :visible.sync="open" width="780px"
               :close-on-click-modal="false" append-to-body @close="graphTypes = undefined">
      <el-form ref="form" :model="form"  label-width="80px">
        <el-form-item label="生成类型" prop="graphType" class="errTipIcon">
          <el-radio-group v-model="form.graphType" @change="changeGroup">
            <el-radio :label="0">一键智能构建图谱</el-radio>
            <el-radio :label="1">普通配置构建图谱</el-radio>
          </el-radio-group>
          <p v-if="graphTypes === 0" class="red_info">
            只需提供数据文档，无需其他配置，即可快速生成图谱。
          </p>
          <p v-if="graphTypes === 1" class="red_info">
            需通过构建实体、配置数据源、信息抽取、知识映射、知识融合步骤，来构建知识图谱。
          </p>
        </el-form-item>
        <el-form-item label="文档目录" prop="formDirectoryId" class="errTipIcon">
          <el-cascader
            :options="docOptions"
            v-model="formDirectoryId"
            popper-class="kb_cascader_dialog"
            :props="{ value: 'id', label: 'directory', children: 'children',expandTrigger: 'hover',  checkStrictly: true }"
            :show-all-levels="false"
            clearable
            ref="formDirectoryId"
            :key="refresh"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="文档标签" prop="formLabelId" class="errTipIcon">
          <el-cascader
            :options="labelOptions"
            v-model="formLabelId"
            expand-trigger="hover"
            :show-all-levels="false"
            popper-class="kb_cascader_dialog"
            :props="{ value: 'id', label: 'label', expandTrigger: 'hover',children: 'children', checkStrictly: true }"
            :key="refresh"
            clearable>
          </el-cascader>
        </el-form-item>

        <el-form-item label="文档类型" prop="type" class="errTipIcon">
          <el-select
            v-model="form.type"
            placeholder="请选择文档类型"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in relationOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="城市选择" prop="formArea">
          <el-cascader
            :options="areaArr"
            :show-all-levels="false"
            v-model="formArea"
            expand-trigger="hover"
            :props="optionProps"
            @change="changeAreaFrom"
            clearable
            placeholder="请选择城市地区"
            :key="refresh"
          ></el-cascader>
        </el-form-item>

        <el-form-item label="年限范围">
        <el-date-picker
          v-model="formDateStart"
          size="small"
          style="width:103px"
          clearable
          type="year"
          :editable="false"
          :default-value="defaultValue"
          placeholder="开始年"
          format="yyyy"
          value-format="yyyy"
        ></el-date-picker>
        -
        <el-date-picker
          v-model="formDateEnd"
          size="small"
          style="width:103px"
          clearable
          type="year"
          :editable="false"
          placeholder="结束年"
          :default-value="defaultValue"
          format="yyyy"
          value-format="yyyy"
        ></el-date-picker>
       </el-form-item>
       <el-form-item label="模板选择" prop="templateId" >
          <el-select v-model="form.templateId" placeholder="请选择模板" clearable size="small">
            <el-option
              v-for="dict in tempOptions"
              :key="dict.id"
              :label="dict.fileName"
              :value="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上传" prop="fileList" class="errTipIcon">
          <el-upload
            ref="mulUpload"
            action=""
            :on-change="handleChange"
            :on-remove="handleRemove"
            :file-list="fileList"
            :http-request="uploadData"
            :auto-upload="false"
            accept=".doc,.docx,.pdf"
            multiple
          >
            <template #trigger>
              <el-button size="small" type="primary">选取文件</el-button>
            </template>
            <template #tip>
              <div class="el-upload__tip">
                (支持doc/docx/pdf格式文档批量导入)
              </div>
              <div class="el-upload__tip red_info">
                名称不得重复，如待导入文档已存在将新增（知识文档_x）拓展名存入
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="upLoading">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="查看" :visible.sync="kbInfoStatus" width="500px"
               :close-on-click-modal="false" >
      <el-form ref="form" :model="kbInfoForm" label-position="left" label-width="100px">

        <el-form-item label="导入时间：" >
          <span v-if="kbInfoForm.createTime">{{kbInfoForm.createTime}}</span>
          <span v-else> 暂无 </span>
        </el-form-item>
        <el-form-item label="生成时间：" >
          <span v-if="kbInfoForm.graphTime">{{kbInfoForm.graphTime}} </span>
          <span v-else> 暂无 </span>
        </el-form-item>
        <el-form-item label="区域：" prop="areaId">
          <span>{{kbInfoForm.areaId?kbInfoForm.areaId:' 暂无 '}}</span>
        </el-form-item>
        <el-form-item label="年限范围：" >
          <span v-if="kbInfoForm.startDate && kbInfoForm.endDate">{{kbInfoForm.startDate + ' ----- '+ kbInfoForm.endDate}}</span>
          <span v-else>暂无</span>
        </el-form-item>
      </el-form>
      <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="kbInfoStatus = false">确 定</el-button>
      </div> -->
    </el-dialog>
    <el-dialog title="详情" :visible.sync="infoStatus" width="500px" append-to-body height="500px"
               :close-on-click-modal="false">
      <el-form ref="form" :model="infoForm" label-position="left" label-width="100px">

        <el-form-item label="文档标签：" prop="label">
          <el-input type="textarea" v-model="infoForm.label" disabled :autosize="{minRows:3,maxRows:10}"></el-input>
        </el-form-item>
      </el-form>
      <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="infoStatus = false">确 定</el-button>
      </div> -->
    </el-dialog>
    <!--查看文档--->
    <el-dialog title="文档内容" :visible.sync="docStatus"  append-to-body width="80%">
<!--        <p class="arrow">-->
<!--          <span @click="changePdfPage(0)" class="turn" :class="{grey: currentPage==1}">-->
<!--            <i class="el-icon-caret-left"></i>-->
<!--          </span>-->
<!--          {{currentPage}} / {{pageCount}}-->
<!--          <span @click="changePdfPage(1)" class="turn" :class="{grey: currentPage==pageCount}">-->
<!--            <i class="el-icon-caret-right"></i>-->
<!--          </span>-->
<!--        </p>-->
<!--        <div class="pdf_warp">-->
<!--          &lt;!&ndash; <iframe  width="100%" frameborder="0" src="http://vod.qifeisoft.com/456123.html" height="660">-->
<!--          </iframe> &ndash;&gt;-->
<!--          <pdf-->
<!--          :src="src"-->
<!--          :page="currentPage"-->
<!--          @num-pages="pageCount=$event"-->
<!--          @page-loaded="currentPage=$event"-->
<!--          @loaded="loadPdfHandler">-->
<!--          </pdf>-->
<!--        </div>-->
<!--      &lt;!&ndash; <iframe :src="'https://view.officeapps.live.com/op/view.aspx?src=' + this.fileUrl" width="100%" frameborder="0" style="height:81vh;">-->
<!--      </iframe> &ndash;&gt;-->

      <div style="margin:20px auto;height:700px;display: flex">
        <div style="width: 65%; height: 670px; margin: 10px;padding:5px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);display: flex;">
          <div style="width:100%;height:660px;display: flex;">
            <div style="width:100%;height:660px;background-color: #c8c8c8;padding: 30px;overflow-y: scroll;">
              <div id="htmlContent" style="background-color: #FFFFFF;padding: 10px;" v-html="html"></div>
            </div>
          </div>
        </div>
        <div style="width: 35%;horiz-align: center;height: 670px; margin: 10px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);display: flex;">
          <el-tabs type="border-card" style="width:100%;">
            <el-tab-pane label="标签形式">
              <div style="height: 610px"
                   v-loading="rightKbLoading"
                   element-loading-text="知识提取中，请稍候。。。"
                   element-loading-background="rgba(255, 255, 255, 0.8)">
                <el-table
                  :data="tableData"
                  height="610"
                  :show-header="false"
                  :highlight-current-row="false"
                  style="width: 100%">
                  <el-table-column
                    label="序号"
                    type="index"
                    width="60"
                    align="center">
                  </el-table-column>
                  <el-table-column
                    label="知识点">
                    <template slot-scope="scope">
                      <el-tag size="medium" @mouseover.native="setHighlight(scope.row.word1)" @mouseleave.native="setUnHighlight(scope.row.word1)">{{ scope.row.word1 }}</el-tag>
                      <el-tag v-if="!scope.row.relationFlag" size="medium" @mouseover.native="setHighlight(scope.row.relation)" @mouseleave.native="setUnHighlight(scope.row.relation)" color="#f8ffbb" style="color: #a8a60a;">{{ scope.row.relation }}</el-tag>
                      <el-tag size="medium" @mouseover.native="setHighlight(scope.row.word2)" @mouseleave.native="setUnHighlight(scope.row.word2)">{{ scope.row.word2 }}</el-tag>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            <el-tab-pane label="表格形式">
              <div style="height: 610px;overflow-y: auto"
                   v-loading="rightKbLoading"
                   element-loading-text="知识提取中，请稍候。。。"
                   element-loading-background="rgba(255, 255, 255, 0.8)">
                <el-table
                  :data="tableData"
                  height="610"
                  border
                  style="width: 100%">
                  <el-table-column
                    label="序号"
                    type="index"
                    width="60">
                  </el-table-column>
                  <el-table-column
                    prop="word1"
                    label="知识"
                    width="150">
                  </el-table-column>
                  <el-table-column
                    prop="relation"
                    label="关系">
                  </el-table-column>
                  <el-table-column
                    prop="word2"
                    label="知识"
                    width="150">
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>
          <div style="position: absolute;right: 50px;top:100px">
            <el-button size="mini" icon="el-icon-refresh" @click="refreshRight"></el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<style lang="scss" scoped>
.red_info {
  color: #f56c6c;
  margin: 0;
  line-height: 20px;
}
</style>
<script>
  import { listData } from "@/api/system/dict/data"
import { listKB, addKB, updateKB, delKB, statusKB, downloadKB, getHtml } from "@/api/kb/kb";
import { getSubList,subDocumentList } from "@/api/login";
import store from "@/store";
// import areaArr from "@/utils/country-data.js";
import { getInfo } from '@/api/login'
import { templatesList} from "@/api/template/template";
// import pdf from 'vue-pdf'
// import titleTip from "../components/titleTip";
import request from '@/utils/request'
export default {
  name: "Kb",
  components: {pdf,titleTip},
  data() {
    return {
      pageSizes:[10,50,100],
      areaArr,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 角色表格数据
      kbList:null,
      relationOptions:[],
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      com_Id: store.getters.deptId,
      // 日期范围
      dateRange: [],
      // 模板管理
      tempOptions: [],
      searchArea: [],
      labelValArr: [],
      docValArr: [],

      formArea: [],
      formDateStart:"",
      formDateEnd:"",
      formDirectoryId: [],
      formLabelId: [],
      userType:undefined,
      userId:undefined,
      subList: [],
      optionProps: {
        value: "value",
        label: "label",
        children: "children",
        expandTrigger: 'hover',
      },
      // 菜单列表
      menuOptions: [],
      docOptions: [],
      labelOptions: [],
      graphTypeOptions:[
        {
          value:"0",
          label:"智能构建"
        },
        {
          value:"1",
          label:"普通构建"
        },
      ],
      kbInfoStatus:false,
      infoStatus:false,
      kbInfoForm:[],
      infoForm:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        graphType:undefined,
        fileName: undefined,
        directoryId: undefined,
        labelId: undefined,
        areaId: undefined,
        userId: undefined,
        templateId:undefined,
        startDate:undefined,
        endDate:undefined,
        type:undefined,
      },
      graphTypes:null,
      // 表单参数
      form: {
        graphType: undefined,
        templateId:undefined,
        areaId:undefined,
        type:undefined
      },
      formData: null,
      fileList: [],
      // 表单校验
      rules: {
        // graphType: [
        //   { required: true, message: "生成方式不能为空", trigger: "change" }
        // ],
        // formDirectoryId: [
        //   {
        //     required: true,
        //     message: "文档目录不能为空",
        //     trigger: "change",
        //     validator: (rule, value, callback) => {
        //       this.formDirectoryId.length > 0
        //         ? callback()
        //         : callback(new Error("文档目录不能为空"));
        //     }
        //   }
        // ],
        // formLabelId: [
        //   {
        //     required: true,
        //     message: "文档标签不能为空",
        //     trigger: "change",
        //     validator: (rule, value, callback) => {
        //       this.formLabelId.length > 0
        //         ? callback()
        //         : callback(new Error("文档标签不能为空"));
        //     }
        //   }
        // ],
        // fileList: [
        //   {
        //     required: true,
        //     message: "上传文件不能为空",
        //     trigger: "change",
        //     validator: (rule, value, callback) => {
        //       this.fileList.length > 0
        //         ? callback()
        //         : callback(new Error("文档标签不能为空"));
        //     }
        //   }
        // ]
        // directoryId: [
        //   { required: true, message: "文档目录不能为空", trigger: "change" }
        // ],
        // labelId: [
        //   { required: true, message: "标签不能为空", trigger: "change" }
        // ],
        // areaId: [{ required: true, message: "区域不能为空", trigger: "change" }]
      },
      relationData: [
        {
          key: "1.关系下载",
          value: "relationship",
          id: 1,
          name: "关系下载.json"
        },
        {
          key: "2.信息抽取",
          value: "informationExtraction",
          id: 2,
          name: "信息抽取.json"
        },
        {
          key: "3.知识映射",
          value: "knowledgeMapping",
          id: 3,
          name: "知识映射.json"
        },
        {
          key: "4.知识融合",
          value: "knowledgeFusion",
          id: 4,
          name: "知识融合.json"
        }
      ],
      upLoading:false,
      refreshItem: 0,//第一次刷新
      refresh:0,//导入时间刷新
      defaultValue:new Date(),
      docStatus:false,
      fileUrl:'http://www.tjkjxh.org.cn/u/cms/www/202108/16150951jjfd.docx',
      //pdf预览
      currentPage: 0, // pdf文件页码
      pageCount: 0, // pdf文件总页数
      fileType: 'pdf', // 文件类型
　　　 src: 'http://114.116.105.151/prod-api/file/file/download.do?filePath=/mnt/knowledgeGraph-cloud/upload/100/1/100/e06999a2-2d73-41d3-9077-f5da23356d7c/72277ccb-c547-4ff3-bb40-ca263d0ef0cf.pdf&fileName=Jetson_Xavier_NX_Developer_Kit_User_Guide.pdf&documentId=271', // pdf文件地址

      tableData: [],
      rightKbLoading: false,
      html: '',
      rowId: ''
    };
  },
  created() {
    console.log('获取deptId',store.getters.deptId,)
    getInfo().then(res=>{
      this.userType = res.user.userType
      this.userId = res.user.userId
      this.getList();
      if(this.com_Id){
        this.getLabelDoc();
      }else{
        this.com_Id=res.user.deptId
        this.getLabelDoc();
      }
      subDocumentList().then(r => {
        this.subList = r;
      });
      this.getTemplates()
    })
    // this.getDicts("sys_normal_disable").then(response => {
    //   this.statusOptions = response.data;
    // });

    listData({dictType:'sys_document_type'}).then(res=>{
      if(res.code == 200){
        this.relationOptions = res.rows
      }
    })
  },
  destroy() {
    this.$eventBus.$off('getLabelDocSelect')
    this.$eventBus.$off('getTemplatesSelect')
  },
  filters: {
    getStatus: function(val) {
      let str = "";
      switch (val) {
        case 0:
          str = "生成图谱";
          break;
        case 2:
          str = "生成中";
          break;
        case 3:
          str = "生成中";
          break;
        case 1:
          str = "预览图谱";
          break;
        default:
          str = "数据异常";
          break;
      }
      return str;
    }
  },
  mounted () {
    setInterval(function () {
      document.querySelectorAll('.el-cascader-node__label').forEach(el => {
        el.onclick = function () {
          if (this.previousElementSibling) this.previousElementSibling.click()
        }
      })
    }, 1000)
    let that=this
    this.$eventBus.$on("getLabelDocSelect",() => {
      that.getLabelDoc();
    })
    this.$eventBus.$on("getTemplatesSelect",() => {
      that.getTemplates();
    })
  },
  methods: {
    setHighlight(title) {
      let htmlContent = document.getElementById('htmlContent');
      let innerHtml = htmlContent.innerHTML
      let reg = new RegExp(title, "g");
      innerHtml = innerHtml.replace(reg, '<em style="background-color: #ee673e">' + title + "</em>")
      htmlContent.innerHTML = innerHtml
    },
    setUnHighlight(title) {
      let htmlContent = document.getElementById('htmlContent');
      let innerHtml = htmlContent.innerHTML
      let reg = new RegExp('<em style="background-color: #ee673e">' + title + "</em>", "g");
      innerHtml = innerHtml.replace(reg, title)
      htmlContent.innerHTML = innerHtml
    },
    getTemplates(){
      let queryParams={
        page: 1,
        pageSize:999,
      }
      templatesList(queryParams).then(response => {
        this.tempOptions=response.data.list
      }).catch((res) => {
        this.$message.error(res.msg);
      });
    },
    handleDoc(row){
      // this.src = pdf.createLoadingTask(row.fileUrl+'&documentId='+row.id)
      this.rightKbLoading = true
      this.html = ""
      this.tableData = []
      getHtml(row.id)
        .then(response => {
          if (response.data) {
            this.docStatus=true
            this.html = response.data
          } else {
            this.$message.warning(`该文件不存在！`);
          }
        });
      let kbUrl = cfg.kbURL
      this.rowId = row.id
      request({
        url: `${kbUrl}?documentId=${row.id}`,
        method: 'get',
      }).then(res=>{
        this.tableData = res.data
        this.rightKbLoading = false
      })
    },
    refreshRight(){
      this.rightKbLoading = true
      let kbUrl = cfg.kbURL
      request({
        url: `${kbUrl}?documentId=${this.rowId}&refreshFlag=1`,
        method: 'get',
      }).then(res=>{
        this.tableData = res.data
        this.rightKbLoading = false
      })
    },
    iconStatus(val){
      let icon = "";
      switch (val) {
        case 0:
          icon = "el-icon-share";
          break;
        case 2:
          icon = "";
          break;
        case 3:
          icon = "";
          break;
        case 1:
          icon = "el-icon-view";
          break;
        default:
          icon = "";
          break;
      }
      return icon;
    },
    getInfo(){
      getInfo().then(res=>{
        this.userType = res.user.userType
        this.userId = res.user.userId
      })
    },
    parseTimes(time, pattern){
      if (arguments.length === 0 || !time) {
        return null
      }
      const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
      let date
      if (typeof time === 'object') {
        date = time
      } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
          time = parseInt(time)
        } else if (typeof time === 'string') {
          time = time.replace(new RegExp(/-/gm), '/');
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
          time = time * 1000
        }
        date = new Date(time)
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      }
      const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key]
        // Note: getDay() returns 0 on Sunday
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        if (result.length > 0 && value < 10) {
          value = '0' + value
        }
        return value || 0
      })
      return time_str
    },
    filterUser(val) {
      // console.log(val);
      if(val.userId){
        return this.subList.filter(v=>v.userId == val.userId)[0]?
          this.subList.filter(v=>v.userId == val.userId)[0].userName:""
        // console.log(this.subList,111);
      }else{
        return ""
      }
    },
    filterType(val) {
      // console.log(val);
      if(val.type){
        return this.relationOptions.filter(v=>v.dictValue == val.type)[0]?
          this.relationOptions.filter(v=>v.dictValue == val.type)[0].dictLabel:""
        // console.log(this.subList,111);
      }else{
        return ""
      }
    },
    downloadRelation(item) {
      console.log("item", item);
      let name = item.name;
      let id = item.id;
      this.download(`file/file/getHelpFile.do?id=${id}`, {}, name, "get");
    },
    kbResult(row){
      this.$router.push({path:"/kbMange/kbResult",query:{docId:row.id}})
    },
    kbInfo(row){
      this.kbInfoStatus = true
      this.kbInfoForm = {...row}
      this.kbInfoForm.startDate = this.kbInfoForm.startDate?this.kbInfoForm.startDate.substring(0,10):''
      this.kbInfoForm.endDate = this.kbInfoForm.endDate?this.kbInfoForm.endDate.substring(0,10):''
    },
    info(row){
       this.infoStatus = true
       this.infoForm = {...row}
    },
    downloadDoc(row) {
      let fileId = row.id;
      //row.fileName.slice(0, row.fileName.lastIndexOf(".") - 1) + ".json";
      var file_name =
        row.fileName.slice(0, row.fileName.lastIndexOf(".")) + ".json";
      this.download(
        `file/entity/downloadGraph.do?fileId=${fileId}`,
        {},
        file_name,
        "get"
      );
    },
    changeStatusKB(row) {
      if(row.status == 2){
        return
      }
      if(row.status === 1){
        this.$router.push({name:'TenantGraph',params:{row}})
        // console.log(this.$router);
        return;
      }
      statusKB(row.id).then(() => {
        this.getList();
        this.msgSuccess("操作成功");
      });
    },
    getStr(val) {
      let arr = "";
      let len = val.length;
      val.map((ele, index) => {
        if (len - 1 == index) {
          arr += ele;
        } else {
          arr += ele + ",";
        }
      });
      return arr;
    },
    changeArea(val) {
      this.queryParams.areaId = this.getStr(val);
    },
    changeAreaFrom(val) {
      this.form.areaId = this.getStr(val);
    },
    uploadData(params) {
      let gId = this.form.graphType;
      let dId = this.formDirectoryId || [];
      let lId = this.formLabelId || [];
      let aId = this.form.areaId;
      let date = this.formDateRange;
      let directoryId = dId[dId.length - 1];
      let labelId = lId[lId.length - 1];
      let type = this.form.type;
      let templateId = this.form.templateId;

      let formData = this.formData;
      !!formData.get("graphType") && gId
          ? formData.set("graphType", gId)
          : formData.append("graphType", gId);
      !!formData.get("directoryId") && dId
        ? formData.set("directoryId", directoryId)
        : formData.append("directoryId", directoryId);
      !!formData.get("labelId") && lId
        ? formData.set("labelId", labelId)
        : formData.append("labelId", labelId);
      if(aId){
        !!formData.get("areaId") && aId
          ? formData.set("areaId", aId)
          : formData.append("areaId", aId);
      }
      // console.log(,1111111111);
      if(this.formDateStart && this.formDateEnd>0){
        !!formData.get("startDate") && this.formDateStart
          ? formData.set("startDate", this.formDateStart)
          : formData.append("startDate", this.formDateStart);
        !!formData.get("endDate") && this.formDateEnd
          ? formData.set("endDate", this.formDateEnd)
          : formData.append("endDate", this.formDateEnd);
      }
      if(type){
        !!formData.get("type") && type
          ? formData.set("type", type)
          : formData.append("type", type);
      }
      if(templateId){
        !!formData.get("templateId") && templateId
          ? formData.set("templateId", templateId)
          : formData.append("templateId", templateId);
      }
      console.log(formData.get("type"), "type aaaa ");
    },
    handleRemove(file, fileList) {
      this.handleChange(file, fileList);
      // console.log("handleRemove", file, fileList);
    },
    handleChange(file, fileList) {
      this.formData = new FormData();
      let formData = this.formData;
      this.fileList = fileList;
      this.$refs.form.validateField("fileList");
      fileList.forEach(ele => {
        formData.append("files", ele.raw, ele.name);
      });

      // this.form.files = formData;
      // console.log("form", this.form);
    },
    // 获取文档和标签下拉列表
    getLabelDoc() {
      DOC.docSearch({}).then(r => {
        this.docOptions = r.data;
      });
      DOC.labelSearch({}).then(r => {
        this.labelOptions = r.data;
      });
    },
    /** 查询角色列表 */
    getList() {
      console.log(this.queryParams.graphType);
      console.log(typeof this.queryParams.graphType,"type");
      this.loading = true;
      let date = this.dateRange;
      if(date){
        this.queryParams.createdTimeStart = date.length > 0 ? date[0] : undefined;
        this.queryParams.createdTimeEnd = date.length > 0 ? date[1] : undefined;
      }else{
        this.queryParams.createdTimeStart = undefined
        this.queryParams.createdTimeEnd = undefined
      }
      console.log(this.userType);
      if(this.userType == '02'){
        this.queryParams.userId = this.userId
      }
      let dId = this.docValArr;
      let lId = this.labelValArr;
      // let type = this.queryParams.type
      this.queryParams.directoryId =
        dId.length > 0 ? dId[dId.length - 1] : undefined;
      this.queryParams.labelId =
        lId.length > 0 ? lId[lId.length - 1] : undefined;
      let queryParams = {...this.queryParams}
      if(queryParams.graphType === 0 || queryParams.graphType === 1) queryParams.graphType = Number(this.queryParams.graphType)
      listKB(queryParams)
        .then(response => {

          this.kbList = response.rows;
          this.total = response.total;
          this.loading = false;
        })
        .catch(e => {
          this.loading = false;
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.graphTypes = undefined
      this.$refs.mulUpload.clearFiles();
      this.reset();
      this.upLoading=false
    },
    // 表单重置
    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([]);
      }
      this.form.graphType=undefined
      this.formDirectoryId=undefined
      this.formLabelId=undefined
      this.form.areaId=undefined
      this.form.templateId=undefined
      this.form.type=undefined

      this.defaultValue=new Date()
      this.refresh++
      this.formArea = [];
      this.formDateStart ='';
      this.formDateEnd ='';
      this.formDirectoryId = [];
      this.formLabelId = [];
      this.fileList = [];
      this.resetForm("form");
      //this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if(this.queryParams.startDate&&!this.queryParams.endDate){
        this.$message.error('请选择年限范围的结束时间')
      }else if(!this.queryParams.startDate&&this.queryParams.endDate){
        this.$message.error('请选择年限范围的开始时间')
      }else if(this.queryParams.endDate&&this.queryParams.startDate&&this.queryParams.startDate>this.queryParams.endDate){
        this.$message.error('年限范围的开始时间不能大于结束时间')
      }else{
        this.queryParams.pageNum = 1;
        this.getList();
      }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.refreshItem++
      this.dateRange = [];
      this.searchArea = [];
      this.labelValArr = [];
      this.docValArr = [];
      this.defaultValue=new Date()
      this.queryParams.startDate=undefined;
      this.queryParams.endDate=undefined;
      this.queryParams.graphType = undefined
      this.queryParams.fileName = undefined
      this.queryParams.directoryId = undefined
      this.queryParams.labelId = undefined
      this.queryParams.areaId = undefined
      this.queryParams.createdTimeStart = undefined
      this.queryParams.createdTimeEnd = undefined
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
    },
    /** 分配数据权限操作 */
    changeGraphId(row) {
      this.$prompt("请输入图谱ID", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        closeOnClickModal:false,
        inputErrorMessage: "图谱ID不能为空"
      })
        .then(({ value }) => {
          updateKB(row.id, value).then(() => {
            this.$message({
              type: "success",
              message: "操作成功！"
            });
            this.getList();
          });
        })
        .catch(() => {});
    },
    changeGroup(val){
      console.log(val);
      // this.$forceUpdate()
      this.graphTypes = val
      // console.log(val);
    },
    /** 提交按钮 */
    submitForm() {
      if(this.form.graphType==undefined){
        this.$message.error('生成方式不能为空');
        return false;
      }else if(this.formDirectoryId.length<1){
        this.$message.error('文档目录不能为空');
        return false;
      }else if(this.formLabelId.length==0){
        this.$message.error('文档标签不能为空');
        return false;
      }else if(this.form.type==undefined){
        this.$message.error('文档类型不能为空');
        return false;
      }else if(this.fileList.length<1){
        this.$message.error('上传文件不能为空');
        return false;
      }
      if(this.formDateStart&&!this.formDateEnd){
        this.$message.error('请选择年限范围的结束时间')
        return false;
      }else if(!this.formDateStart&&this.formDateEnd){
        this.$message.error('请选择年限范围的开始时间')
        return false;
      }else if(this.formDateStart&&this.formDateEnd&&this.formDateStart>this.formDateEnd){
        this.$message.error('年限范围的开始时间不能大于结束时间')
        return false
      }
      this.$refs.mulUpload.submit();
      this.$refs["form"].validate(valid => {
        if (!valid) return false;
        this.upLoading=true
        console.log(this.formData,111111);
        addKB(this.formData).then(response => {
          this.msgSuccess("新增成功");
          this.upLoading=false
          this.formData=[]
          this.resetQuery()
          this.cancel()
        }).catch((res) => {
          this.upLoading=false
        });;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      let _this = this;
      _this.$confirm("是否确认删除数据?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          delKB(id).then((response) => {

            if(_this.kbList.length===1){
              _this.queryParams.pageNum=_this.queryParams.pageNum-1
            }
            _this.getList();
            _this.msgSuccess("删除成功");
          });
        })
        .then(() => {});
    },
    // 改变PDF页码,val传过来区分上一页下一页的值,0上一页,1下一页
    changePdfPage (val) {
      // console.log(val)
      if (val === 0 && this.currentPage > 1) {
        this.currentPage--
        // console.log(this.currentPage)
      }
      if (val === 1 && this.currentPage < this.pageCount) {
        this.currentPage++
        // console.log(this.currentPage)
      }
    },
    // pdf加载时
    loadPdfHandler (e) {
      this.currentPage = 1 // 加载的时候先加载第一页
    },
  }
}
</script>
<style lang="scss" >
.pdf_warp{
  height:660px;
  overflow:hidden;
}
  .kb_form_warp .el-table__row{

  }
  .kb_cascader{
    .el-form-item__content{
      width: calc(100% - 68px)!important;
    }
  }

  .kb_cascader_radio .el-cascader-panel .el-radio{
    display: none;
    /*visibility: hidden; // 加上这一行*/
  }
  .kb_cascader_dialog .el-cascader-panel .el-radio{
    display: none;
    /*visibility: hidden; // 加上这一行*/
  }
  .kb_cascader_dialog .el-cascader-menu{
    min-width:215px!important;
    max-width:260px!important;
  }
</style>
