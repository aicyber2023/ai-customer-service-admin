<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <!--      <el-form-item label="模板分类" prop="templateType">-->
      <!--        <el-select v-model="queryParams.templateType" placeholder="请选择模板分类" clearable size="small">-->
      <!--          <el-option-->
      <!--            v-for="dict in typeOptions"-->
      <!--            :key="dict.id"-->
      <!--            :label="dict.name"-->
      <!--            :value="dict.id"-->
      <!--          />-->
      <!--        </el-select>-->
      <!--        <el-link type="primary" icon="el-icon-setting" size="mini" :underline="false" style="margin-left: 5px" @click="gotoType"></el-link>-->
      <!--      </el-form-item>-->
      <el-form-item label="员工模板名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入员工模板名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="当前状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择当前状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增
        </el-button
        >
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          plain-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--        >修改-->
      <!--        </el-button-->
      <!--        >-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
        >删除
        </el-button
        >
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="tableData"
      default-expand-all
      @selection-change="handleSelectionChange"
      height="600px"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="模板分类" width="100" align="center">-->
      <!--        <template slot-scope="scope">-->
      <!--          {{ scope.row.templateType | filterType(typeOptions) }}-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="name"
        label="员工模板名称"
        align="center"
        min-width="100"
      ></el-table-column>
      <el-table-column prop="avatar" label="头像" align="center" width="80">
        <template slot-scope="scope">
          <el-image
            style="height: 40px; margin-bottom: -8px"
            :src="getUrl(scope.row.id)"
            fit="fill"
          >
            <div slot="error" class="image-slot" style="line-height: 40px">
              <!--              <el-image :src="require('@/assets/images/default.jpg')" fit="fill"></el-image>-->
              无
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
        prop="temperature"
        label="随机性"
        align="center"
        width="90"
      >
        <template slot="header" slot-scope="scope">
          随机性
          <el-tooltip
            class="item"
            effect="dark"
            content="值越大，回复越随机"
            placement="right"
          >
            <i class="el-icon-question el-icon--right"/>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="maxTokens"
        label="单次回复限制"
        align="center"
        width="120"
      >
        <template slot="header" slot-scope="scope">
          单次回复限制
          <el-tooltip
            class="item"
            effect="dark"
            content="单次交互所用的最大 Token 数"
            placement="right"
          >
            <i class="el-icon-question el-icon--right"/>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          {{ scope.row.maxTokens ? scope.row.maxTokens + " Tokens" : "无限制" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="presencePenalty"
        label="话题新鲜度"
        align="center"
        width="110"
      >
        <template slot="header" slot-scope="scope">
          话题新鲜度
          <el-tooltip
            class="item"
            effect="dark"
            content="值越大，越有可能扩展到新话题"
            placement="right"
          >
            <i class="el-icon-question el-icon--right"/>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="frequencyPenalty"
        label="频率惩罚度"
        align="center"
        width="110"
      >
        <template slot="header" slot-scope="scope">
          频率惩罚度
          <el-tooltip
            class="item"
            effect="dark"
            content="值越大，越有可能降低重复字词"
            placement="right"
          >
            <i class="el-icon-question el-icon--right"/>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="historyMessageCount"
        label="附带历史消息数"
        align="center"
        width="140"
      >
        <template slot="header" slot-scope="scope">
          附带历史消息数
          <el-tooltip
            class="item"
            effect="dark"
            content="每次请求携带的历史消息数"
            placement="right"
          >
            <i class="el-icon-question el-icon--right"/>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        prop="lang"
        label="语言"
        align="center"
        width="50"
      ></el-table-column>
      <!--      <el-table-column prop="introduce" label="角色介绍" align="center" min-width="200"></el-table-column>-->
      <!--      <el-table-column prop="content" label="角色描述" align="center" min-width="100"></el-table-column>-->
      <!--      <el-table-column prop="sample" label="对话样例" align="center" min-width="100"></el-table-column>-->
      <!--      <el-table-column prop="callText" label="招呼语" align="center" min-width="100"></el-table-column>-->
      <el-table-column prop="status" label="当前状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | filterCss(statusOptions)">
            {{ scope.row.status | filterStage(statusOptions) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        align="center"
        width="180"
      ></el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="220"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleChangeStatus(scope.row, 1)"
            v-if="scope.row.status != 1"
          >启用
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleChangeStatus(scope.row, 2)"
            v-else
          >停用
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            :disabled="scope.row.status == 1"
          >删除
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="1100px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="11">
            <el-form-item label="员工模板名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入员工模板名称"
                v-if="editMode"
                :disabled="prohibit"
              ></el-input>
              <span style="margin-left: 10px" v-else>{{ form.name }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-form-item label="角色头像">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :http-request="requestUpload"
                :before-upload="beforeUpload"
                v-if="editMode"
              >
                <el-avatar
                  :size="35"
                  v-if="form.headImageUrl"
                  :src="form.headImageUrl"
                ></el-avatar>
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <el-avatar
                v-else-if="!editMode && form.headImageUrl"
                :size="35"
                :src="form.headImageUrl"
              ></el-avatar>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 公司名称和logo -->
        <el-row>
          <el-col :span="11">
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="form.companyName"
                placeholder="请输入公司名称"
                v-if="editMode"
              ></el-input>
              <span style="margin-left: 10px" v-else>{{
                  form.companyName
                }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-form-item label="公司Logo">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :http-request="requestUpload"
                :before-upload="beforeUploadCompanyLogo"
                v-if="editMode"
              >
                <el-avatar
                  :size="35"
                  v-if="form.CompanyLogo"
                  :src="form.CompanyLogo"
                ></el-avatar>
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <el-avatar
                v-else-if="!editMode && form.CompanyLogo"
                :size="35"
                :src="form.CompanyLogo"
              ></el-avatar>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="11">
            <el-form-item label="员工模板介绍" prop="introduction">
              <el-input
                type="textarea"
                :rows="3"
                resize="none"
                v-model="form.introduction"
                placeholder="请输入员工模板介绍"
                v-if="editMode"
              />
              <span style="margin-left: 10px" v-else>{{
                  form.introduction
                }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-form-item label="开场白" prop="greeting">
              <el-input
                type="textarea"
                :rows="3"
                resize="none"
                v-model="form.greeting"
                placeholder="请输入开场白"
                v-if="editMode"
              />
              <span style="margin-left: 10px" v-else>{{ form.greeting }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="携带文本">
              <div v-if="form.context && form.context.length > 0">
                <el-row v-for="(ele, index) in form.context" :key="ele.id">
                  <el-col :span="2" style="text-align: center">
                    <p
                      style="
                        font-size: 8px;
                        line-height: 16px;
                        margin: 10px 0 0 0;
                      "
                    >
                      {{ ele.role }}：
                    </p>
                  </el-col>
                  <el-col :span="20">
                    <p
                      style="
                        font-size: 8px;
                        line-height: 16px;
                        margin: 10px 0 0 0;
                      "
                    >
                      {{ ele.content }}
                    </p>
                  </el-col>
                  <el-col :span="2" v-if="editMode">
                    <el-button
                      style="width: 17px; height: 17px; padding: 1px"
                      type="primary"
                      plain
                      size="mini"
                      circle
                      icon="el-icon-plus"
                      @click="addSample(index + 1)"
                    ></el-button>
                    <el-button
                      style="
                        width: 17px;
                        height: 17px;
                        padding: 1px;
                        margin-left: 5px;
                      "
                      type="primary"
                      plain
                      size="mini"
                      circle
                      icon="el-icon-minus"
                      @click="removeSample(index)"
                    ></el-button>
                  </el-col>
                </el-row>
              </div>
              <div v-else>
                <el-button
                  style="width: 17px; height: 17px; padding: 1px"
                  type="primary"
                  plain
                  size="mini"
                  circle
                  icon="el-icon-plus"
                  @click="addSample(0)"
                  v-if="editMode"
                ></el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="随机性" prop="temperature">
              <el-slider
                v-model="form.temperature"
                :min="0"
                :max="1"
                :step="0.1"
                style="padding: 0 10px"
                :disabled="!editMode"
              >
              </el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-form-item label="单次回复限制" prop="maxTokens">
              <div v-if="editMode">
                <el-input-number
                  v-model="form.maxTokens"
                  :min="0"
                  :max="4096"
                  :controls="false"
                  style="padding: 0 10px; width: 100px"
                >
                </el-input-number>
                Tokens
                <span style="color: #F56C6C">（不输或输入0则为无限制）</span>
              </div>
              <span style="margin-left: 10px" v-else>{{
                  form.maxTokens ? form.maxTokens + " Tokens" : "无限制"
                }}</span>
            </el-form-item>
          </el-col>

          <el-col :span="11">
            <el-form-item label="话题新鲜度" prop="presencePenalty">
              <el-slider
                v-model="form.presencePenalty"
                :min="-2"
                :max="2"
                :step="0.1"
                style="padding: 0 10px"
                :disabled="!editMode"
              >
              </el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="11" :offset="1">
            <el-form-item label="频率惩罚度" prop="frequencyPenalty">
              <el-slider
                v-model="form.frequencyPenalty"
                :min="-1"
                :max="1"
                :step="0.1"
                style="padding: 0 10px"
                :disabled="!editMode"
              >
              </el-slider>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="附带历史消息数" prop="historyNum">
          <el-input-number
            :min="0"
            :max="64"
            controls-position="right"
            size="small"
            v-model="form.historyMessageCount"
            placeholder="请输入附带历史消息数"
            v-if="editMode"
          />
          <span style="margin-left: 10px" v-else>{{
              form.historyMessageCount
            }}</span>
          条
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="small" v-if="editMode">
            <el-radio v-for="dict in statusOptions" :key="dict.id" :label="dict.dictValue">{{
                dict.dictLabel
              }}
            </el-radio>
          </el-radio-group>
          <el-tag :type="form.status | filterCss(statusOptions)" v-else>
            {{ form.status | filterStage(statusOptions) }}
          </el-tag>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog
      title="追加对话样例"
      :visible.sync="openSample"
      :close-on-click-modal="false"
      width="600px"
      append-to-body
    >
      <el-form
        ref="formSample"
        :model="formSample"
        :rules="rulesSample"
        label-width="80px"
      >
        <el-form-item label="文本类型" prop="role">
          <el-select
            v-model="formSample.role"
            placeholder="请选择文本类型"
            clearable
            size="small"
          >
            <el-option label="system" value="system"/>
            <el-option label="user" value="user"/>
            <el-option label="assistant" value="assistant"/>
          </el-select>
        </el-form-item>
        <el-form-item label="文本内容" prop="content">
          <el-input
            type="textarea"
            :rows="3"
            resize="none"
            v-model="formSample.content"
            placeholder="请输入文本内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="pushSample">确 定</el-button>
        <el-button @click="cancelSample">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {add, del, get, list, update, uploadFace, uploadLogo,} from "@/api/template/template";
import {listData} from "@/api/system/dict/data";
import avatarFile from "@/utils/baseAvatarFile"

export default {
  name: "index",
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: "",
        status: undefined,
      },
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      statusOptions: [],
      typeOptions: [],
      showSearch: true,
      title: "新增角色模板",
      open: false,
      editMode: false,
      faceFormData: undefined,
      LogoFormData: undefined,
      form: {
        id: undefined,
        name: "",
        introduction: undefined,
        companyName: "",
        CompanyLogo: undefined,
        model: undefined,
        context: [],
        greeting: undefined,
        temperature: 0,
        maxTokens: 0,
        sendMemory: undefined,
        frequencyPenalty: 0,
        presencePenalty: 0,
        historyMessageCount: 4,
        compressMessageLengthThreshold: undefined,
        lang: "cn",
        builtin: true,
        status: "0",
        headImageUrl: undefined,
        //   禁止修改模板名称
        prohibit: false
      },
      rules: {
        name: [
          {required: true, message: "请输入员工模板名称", trigger: "blur"},
        ],
        // templateType:[{required:true, message:"请选择模板分类", trigger:"change"}],
        introduction: [
          {required: true, message: "请输入员工模板介绍", trigger: "blur"},
        ],
        greeting: [
          {required: true, message: "请输入开场白", trigger: "blur"},
        ],
      },
      openSample: false,
      formSample: {
        idx: undefined,
        role: "",
        content: "",
      },
      rulesSample: {
        role: [
          {required: true, message: "请选择文本类型", trigger: "change"},
        ],
        content: [
          {required: true, message: "请输入文本内容", trigger: "blur"},
        ],
      },
    };
  },
  filters: {
    filterStage: function (val, list) {
      if (val == 0) val = 2
      if (list && list.length != 0) {
        return list.filter((v) => v.dictValue == val)[0].dictLabel;
      }
      return val;
    },
    filterCss: function (val, list) {
      if (val == 0) val = 2
      if (list && list.length != 0) {
        return list.filter((v) => v.dictValue == val)[0].listClass;
      }
      return val;
    },
    filterType: function (val, list) {
      if (val && list && list.length != 0) {
        return list.filter((v) => v.id == val)[0].name;
      }
      return val;
    },
  },
  methods: {
    getUrl(templateId) {
      let baseUrl = window.cfg.baseUrl;
      let url = baseUrl + "/de/employeeTemplate/showAvatar/" + templateId;
      // +
      // "?uuid=" +
      // Math.random();
      return url;
    },
    getList() {
      list(this.queryParams).then((res) => {
        console.log("111");
        if (res.code == 200) {
          this.tableData = res.rows;
          this.total = res.total;
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.name = "";
      this.queryParams.status = undefined;
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = [];
      let flg = false;
      selection.forEach((ele) => {
        this.ids.push(ele.id);
        if (ele.status == 1) {
          flg = true;
        }
      });
      this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length || flg;
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let form = {...this.form};
          if (this.form.id) {
            form.id = this.form.id;
            update(form).then((res) => {
              if (res.code == 200) {
                if (this.faceFormData && this.LogoFormData) {
                  console.log("头像和公司logo都修改");
                  Promise.all([
                    uploadFace(this.faceFormData),
                    uploadLogo(this.LogoFormData),
                  ]).then(() => {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                  });
                } else if (this.faceFormData) {
                  console.log("头像修改");
                  uploadFace(this.faceFormData).then(() => {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                  });
                } else if (this.LogoFormData) {
                  console.log("logo修改");
                  uploadLogo(this.LogoFormData).then(() => {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                  });
                } else {
                  this.$modal.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
                }
              }
            });
          } else {
            if (!this.faceFormData) {
              const blob = this.dataURItoBlob(avatarFile.baseAvatar);
              let file = new File([blob], 'default-image.jpg', {type: 'image/jpeg'});
              this.faceFormData = new FormData();
              this.faceFormData.append("file", file)
              console.log("头像修改", this.faceFormData)
            }
            if (!this.LogoFormData) {
              const blob = this.dataURItoBlob(avatarFile.baseCompanyAvatar);
              let file = new File([blob], 'default-image.jpg', {type: 'image/jpeg'});
              this.LogoFormData = new FormData();
              this.LogoFormData.append("file", file)
              console.log("logo修改", this.LogoFormData)
            }
            add(form).then((res) => {
              if (res.code == 200) {
                if (this.faceFormData && this.LogoFormData) {
                  this.faceFormData.append("templateId", res.data.id);
                  this.LogoFormData.append("templateId", res.data.id);
                  Promise.all([
                    uploadFace(this.faceFormData),
                    uploadLogo(this.LogoFormData),
                  ]).then(() => {
                    this.$modal.msgSuccess("操作成功");
                    this.open = false;
                    this.getList();
                    this.faceFormData = null
                    this.LogoFormData = null
                  });
                  // uploadFace(this.faceFormData).then(response => {
                  //   this.$modal.msgSuccess("操作成功");
                  //   this.open = false;
                  //   this.getList();
                  // });
                } else {
                  this.$modal.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
                  this.faceFormData = null
                  this.LogoFormData = null
                }
              }
            });
          }
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    handleAdd() {
      this.prohibit = false;
      this.reset();
      this.open = true;
      this.title = "新增员工模板";
      this.editMode = true;
    },
    handleView(row) {
      this.reset();
      let id = row.id || this.ids;
      get(id).then((response) => {
        this.form = response.data;
        this.form.status = this.form.status + "";
        this.open = true;
        this.title = "员工模板详情";
        this.editMode = false;
        if (this.form.avatarContentType) {
          this.form.headImageUrl =
            window.cfg.baseUrl +
            "/de/employeeTemplate/showAvatar/" +
            this.form.id;
          this.form.CompanyLogo =
            window.cfg.baseUrl +
            "/de/employeeTemplate/showCompanyAvatar/" +
            this.form.id;
        }
      });
    },
    handleUpdate(row) {
      this.prohibit = true
      this.reset();
      let id = row.id || this.ids;
      get(id).then((response) => {
        this.form = response.data;
        this.form.status = this.form.status + "";
        this.open = true;
        this.title = "修改员工模板";
        this.editMode = true;
        if (this.form.avatarContentType) {
          this.form.headImageUrl =
            window.cfg.baseUrl +
            "/de/employeeTemplate/showAvatar/" +
            this.form.id;
          this.form.CompanyLogo =
            window.cfg.baseUrl +
            "/de/employeeTemplate/showCompanyAvatar/" +
            this.form.id;
        }
      });
    },
    handleDel(row) {
      const ids = row.id || this.ids;
      this.$confirm("确定要删除选中的员工模板吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          del(ids).then((res) => {
            if (res.code == 200) {
              this.$modal.msgSuccess("操作成功");
              this.open = false;
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleChangeStatus(row, val) {
      let form = {
        id: row.id,
        status: val,
      };
      update(form).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("操作成功");
          this.getList();
        }
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: "",
        introduction: undefined,
        model: undefined,
        context: [],
        greeting: undefined,
        temperature: 0.5,
        maxTokens: 0,
        sendMemory: undefined,
        frequencyPenalty: 0,
        presencePenalty: 0,
        historyMessageCount: 4,
        compressMessageLengthThreshold: undefined,
        lang: "cn",
        builtin: true,
        status: "0",
        headImageUrl: undefined,
      };
      this.resetForm("form");
    },
    requestUpload() {
    },
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.$modal.msgError(
          "文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。"
        );
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          console.log(reader.result);
          this.form.headImageUrl = reader.result;
          this.$forceUpdate();
          this.faceFormData = new FormData();
          this.faceFormData.append("file", file);
          if (this.form.id) {
            this.faceFormData.append("templateId", this.form.id);
          }
        };
      }
    },
    beforeUploadCompanyLogo(file) {
      if (file.type.indexOf("image/") == -1) {
        this.$modal.msgError(
          "文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。"
        );
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          console.log(reader.result);
          this.form.CompanyLogo = reader.result;
          this.$forceUpdate();
          this.LogoFormData = new FormData();
          this.LogoFormData.append("file", file);
          if (this.form.id) {
            this.LogoFormData.append("templateId", this.form.id);
          }
        };
      }
    },
    addSample(index) {
      this.formSample = {
        idx: index,
        role: "",
        content: "",
      };
      this.resetForm("formSample");
      this.openSample = true;
    },
    removeSample(index) {
      this.form.context.splice(index, 2);
    },
    pushSample() {
      this.$refs["formSample"].validate((valid) => {
        if (valid) {
          this.form.context.splice(this.formSample.idx, 0, {
            role: this.formSample.role,
            content: this.formSample.content,
          });
          this.openSample = false;
        }
      });
    },
    cancelSample() {
      this.openSample = false;
      this.formSample = {
        idx: undefined,
        role: "",
        content: "",
      };
    },
    dataURItoBlob(dataURI) {
      const byteString = atob(dataURI.split(',')[1]);
      const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
      const ab = new ArrayBuffer(byteString.length);
      const ia = new Uint8Array(ab);

      for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
      }

      return new Blob([ab], {type: mimeString});
    }
  },
  created() {
    // 检测类型
    listData({dictType: "biz_employee_template_status"}).then((res) => {
      if (res.code == 200) {
        this.statusOptions = res.rows;
      }
    });
    this.getList();
  },
};
</script>

<style lang="scss">
//.el-pagination {
//  .number:hover {
//    color: #1d93ab !important;
//  }
//
//  .active:hover {
//    color: white !important;
//  }
//}
//
//.el-button--text {
//  color: #1d93ab !important;
//}
//
//.el-button--text:hover {
//  color: #8bb4bc !important;
//}
//
//.el-button--text:focus {
//  color: #1d93ab !important;
//}
//
//.el-button--text:hover:focus {
//  color: #8bb4bc !important;
//}
//
//.el-button--text:active {
//  color: #1d93ab !important;
//}
//
//.el-button--text:hover:active {
//  color: #1d93ab !important;
//
//  i {
//    color: #1d93ab !important;
//  }
//
//  &:hover {
//    color: #8bb4bc !important;
//
//    i {
//      color: #1d93ab !important;
//    }
//  }
//}
//
//.is-disabled {
//  color: #c0c4cc !important;
//
//  i {
//    color: #c0c4cc !important;
//  }
//
//  &:hover {
//    color: #c0c4cc !important;
//
//    i {
//      color: #c0c4cc !important;
//    }
//  }
//}
//
//.el-radio__inner {
//  border-color: #1d93ab !important;
//}
//
//.is-checked .el-radio__inner {
//  background: #1d93ab !important;
//}
//
//.el-radio__label {
//  color: #1d93ab !important;
//}
//
//.el-slider__bar {
//  background: #1d93ab !important;
//}
//
//.el-tooltip {
//  border-color: #1d93ab !important;
//}
//
//.el-checkbox {
//  .el-checkbox__inner:hover {
//    border-color: #1d93ab;
//  }
//
//  .el-checkbox__inner:focus {
//    border-color: #1d93ab;
//  }
//
//  .el-checkbox__inner:active {
//    border-color: #1d93ab;
//  }
//}
//
//.el-checkbox.is-checked {
//  .el-checkbox__inner {
//    background: #1d93ab;
//  }
//
//  .el-checkbox__inner:focus {
//    border-color: #1d93ab;
//  }
//}
//
//.el-checkbox__input.is-indeterminate {
//  .el-checkbox__inner {
//    background: #1d93ab;
//  }
//
//  .el-checkbox__inner:focus {
//    border-color: #1d93ab;
//  }
//}
//
//.el-button--primary {
//  background: #1d93ab !important;
//  color: white !important;
//
//  &:hover {
//    background: rgba(29, 147, 171, 0.7) !important;
//    border-color: #1d93ab !important;
//  }
//
//  &:active {
//    border-color: #1d93ab !important;
//  }
//}
//
//.mb8 {
//  .el-button--primary {
//    background: rgba(29, 147, 171, 0.16) !important;
//    color: #519ba9 !important;
//    border-color: rgba(29, 147, 171, 0.35) !important;
//
//    i {
//      color: #1d93ab !important;
//    }
//
//    &:hover {
//      background: rgba(29, 147, 171, 1) !important;
//      border-color: rgba(29, 147, 171, 1) !important;
//      color: white !important;
//
//      i {
//        color: white !important;
//      }
//    }
//
//    &:active {
//      background: #0e7a90 !important;
//      border-color: #0e7a90 !important;
//    }
//  }
//
//  .el-button--success {
//    background: rgba(249, 190, 3, 0.16) !important;
//    color: #f9be03 !important;
//    border-color: rgba(249, 190, 3, 0.35) !important;
//
//    i {
//      color: #f9be03 !important;
//    }
//
//    &:hover {
//      background: rgba(249, 190, 3, 1) !important;
//      color: white !important;
//      border-color: rgba(249, 190, 3, 1) !important;
//
//      i {
//        color: white !important;
//      }
//    }
//
//    &:active {
//      background: #e99d42 !important;
//      border-color: #e99d42 !important;
//    }
//  }
//
//  .el-button--success.is-disabled {
//    background: rgba(249, 190, 3, 0.16) !important;
//    color: rgba(249, 192, 3, 0.7) !important;
//    border-color: rgba(249, 190, 3, 0.25) !important;
//
//    i {
//      color: rgba(249, 192, 3, 0.7) !important;
//    }
//  }
//
//  .el-button--danger {
//    background: rgba(240, 120, 116, 0.16) !important; //背景色 16%
//    color: #f07974 !important;
//    border-color: rgba(240, 120, 116, 0.35) !important;
//
//    i {
//      color: #f07874 !important;
//    }
//
//    &:hover {
//      background: rgba(240, 120, 116, 1) !important; //背景色 16%
//      color: white !important;
//      border-color: rgba(240, 120, 116, 1) !important;
//
//      i {
//        color: white !important;
//      }
//    }
//
//    &:hover {
//      background: #d9635e !important;
//      border-color: #d9635e !important;
//    }
//  }
//
//  .el-button--danger.is-disabled {
//    background: rgba(240, 120, 116, 0.16) !important; //背景色 16%
//    color: rgba(240, 120, 116, 0.7) !important;
//    border-color: rgba(240, 120, 116, 0.35) !important;
//
//    i {
//      color: rgba(240, 120, 116, 0.7) !important;
//    }
//  }
//}

.el-tag--primary {
  background: rgba(29, 147, 171, 0.16) !important;
  color: #519ba9 !important;
  border-color: rgba(29, 147, 171, 0.35) !important;
}

.el-tag--success {
  background: rgba(249, 190, 3, 0.16) !important;
  color: #f9be03 !important;
  border-color: rgba(249, 190, 3, 0.35) !important;
}

.el-tag--danger {
  background: rgba(240, 120, 116, 0.16) !important; //背景色 16%
  color: #f07974 !important;
  border-color: rgba(240, 120, 116, 0.35) !important;
}

//.el-input__inner:focus {
//  border-color: #1d93ab !important;
//}
//
//.el-textarea__inner:focus {
//  border-color: #1d93ab !important;
//}
//
//.el-button--default {
//  &:hover {
//    color: #1d93ab;
//    border-color: #1d93ab !important;
//  }
//
//  &:active {
//    border-color: #1d93ab !important;
//  }
//
//  &:focus {
//    color: #1d93ab;
//  }
//}
//
//.el-select-dropdown__item:hover {
//  background: #e7f8ff;
//}

.selected {
  color: #1d93ab !important;
}

.review-type {
  padding: 10px 20px;
  height: calc(100vh - 86px);
  overflow-y: auto;
}
</style>
