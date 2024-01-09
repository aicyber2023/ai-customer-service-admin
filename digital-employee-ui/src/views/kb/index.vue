<template>
  <div class="container kb">
    <div @click="closeView" class="closeBtn">
      <i class="el-icon-close"/>
    </div>
    <span class="employeeName" >{{employeeName}}</span>
    <el-tabs v-model="currentType" @tab-click="handleClick">
      <el-tab-pane label="文档库" name="0" style="padding-bottom: 20px">
        <!--      文档库-->
        <el-form :model="queryParams" :inline="true" style="display: flex;align-items: center">
          <el-form-item label="文件名称" style="margin-bottom: 0">
            <el-input
              v-model="queryParams.fileName"
              placeholder="请输入文件名称"
              clearable
              size="small"
            />
          </el-form-item>
          <el-form-item label="训练状态" style="margin-bottom: 0">
            <el-select v-model="queryParams.status" size="small">
              <el-option
                v-for="item in stateOptions"
                :key="item.id"
                :label="item.state"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间" style="margin-bottom: 0">
            <el-date-picker
              v-model="queryParams.dateRangeSendTime"
              :default-time="['12:00:00','12:00:00']"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item style="margin-bottom: 0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="loadingListDQ"
            >搜索
            </el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery('document')"
            >重置
            </el-button
            >
          </el-form-item>
        </el-form>
        <el-divider></el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="uploadTitle='文档库';upload_file_state = true"
            >上传
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multipleDQ"
              @click="delKbFile"
            >删除
            </el-button
            >
          </el-col>
        </el-row>
        <el-table
          :data="tableData"
          default-expand-all
          height="600"
          v-loading="loading"
          @selection-change="handleSelectionChange($event,'DQ')"
        >
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column
            prop="fileName"
            label="文件名"
            align="center"
            min-width="150"
          />
          <el-table-column
            prop="fileSize"
            label="文件大小"
            align="center"
            width="100"
          >
            <template slot-scope="scope">
              {{ scope.row.fileSize | filterFileSizeKB }}
            </template>
          </el-table-column>
          <el-table-column
            prop="trainingStatus"
            label="训练状态"
            align="center"
            width="200"
          >
            <template slot-scope="scope">
              {{ scope.row.status | filterTrainingStatus }}
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            align="center"
            width="150"
          />
          <el-table-column label="操作" align="center" width="250">
            <template slot-scope="scope">
              <el-button type="text" :disabled="scope.row.status==2" style="width: 4rem" @click="initWebSocket(scope.row.id)"
              >{{ scope.row.status|filterOperate }}
              </el-button
              >
              <el-button type="text" @click="delKbFile(scope.row)"
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
          @pagination="loadingListDQ"
        />
      </el-tab-pane>
      <el-tab-pane label="问答库" name="1" style="padding-bottom: 20px">
        <!--      问答库-->
        <el-form v-model="wendaQueryParams" :inline="true" style="display: flex;align-items: center">
          <el-form-item label="用户问" style="margin-bottom: 0">
            <el-input
              v-model="wendaQueryParams.question"
              placeholder="请输入关键词"
              clearable
              size="small"
            />
          </el-form-item>
          <el-form-item label="来源" style="margin-bottom: 0">
            <el-select v-model="wendaQueryParams.createType" size="small">
              <el-option
                v-for="item in wendaStateOptions"
                :key="item.id"
                :label="item.state"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间" style="margin-bottom: 0">
            <el-date-picker
              v-model="wendaQueryParams.dateRangeSendTime"
              :default-time="['12:00:00','12:00:00']"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item style="margin-bottom: 0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="wendaQueryParams.pageNum=1;loadingListWD()"
            >搜索
            </el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery('kv')"
            >重置
            </el-button
            >
          </el-form-item>
        </el-form>
        <el-divider></el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="uploadTitle='问答库';upload_file_state = true"
            >上传
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-edit"
              size="mini"
              @click="wendaCreateAndEditState = true;titleWD='新建'"
            >新建
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multipleWD"
              @click="removeWD"
            >删除
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-s-data"
              size="mini"
              @click="loadingFileRecord"
            >上传记录
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-document-copy"
              size="mini"
              @click="loadingSameProblem"
            >{{ sameProblemStateShow ? "隐藏" : "显示" }}重复问题
            </el-button
            >
          </el-col>
        </el-row>
        <!--        <div style="height: 400px;overflow-y: scroll">-->
        <el-table
          :data="tableDataWD"
          default-expand-all
          height="600"
          v-loading="loading"
          @selection-change="handleSelectionChange($event,'WD')"
        >
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column
            label="用户问"
            align="center"
            min-width="250"
            :show-overflow-tooltip="true"
          >
            <template v-slot="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.question" placement="top"/>
              {{ scope.row.question }}
            </template>
          </el-table-column>
          <el-table-column
            label="回答"
            align="center"
            min-width="250"
            :show-overflow-tooltip="true">
            <template v-slot="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.answer" placement="top"/>
              {{ scope.row.answer }}
            </template>
          </el-table-column>
          <el-table-column
            prop="trainingStatus"
            label="来源"
            align="center"
            width="150"
          >
            <template slot-scope="scope">
              {{ scope.row.createType | filterFileSource }}
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            align="center"
            min-width="150"
          />
          <el-table-column label="操作" align="center" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="updateWDHandler(scope.row)"
              >修改
              </el-button
              >
              <el-button type="text" @click="removeWD(scope.row)"
              >删除
              </el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="totalWD > 0"
          :total="totalWD"
          :page.sync="wendaQueryParams.pageNum"
          :limit.sync="wendaQueryParams.pageSize"
          @pagination="loadingListWD"
        />
      </el-tab-pane>
    </el-tabs>
    <!-- 通用上传文件提示框 -->
    <el-dialog :visible.sync="upload_file_state" @close="resetFileList" width="60%">
      <el-form
        :model="form"
        ref="form"
        :rules="rules"
        label-width="80px"
        :inline="false"
        size="normal"
      >
        <div class="myDialog-body">
          <span class="myDialog-title">上传{{ uploadTitle }}文件</span>
          <div class="myDialog-content">
            <el-form-item label="文件个数" label-width="100px" prop="fileNum">
              {{ form.fileList.length }}/10个
            </el-form-item>
            <el-form-item label="文件" label-width="100px" prop="fileList">
              <el-upload
                multiple
                class="upload-demo"
                ref="upload"
                action="https://jsonplaceholder.typicode.com/posts/"
                :on-change="changeFile"
                :file-list="form.fileList"
                :auto-upload="false"
                :limit="10"
                :on-remove="changeFile"
                :accept="currentType==='0'?'.txt,.word.pdf':'.xls,.xlsx'"
                :on-exceed="exceedFileError"
              >
                <el-button
                  slot="trigger"
                  size="small"
                  type="primary"
                  class="editConfirm"
                >选取文件
                </el-button
                >
                <el-button
                  style="margin-left: 10px; display: none"
                  size="small"
                  type="success"
                  @click="submitUpload"
                >上传到服务器
                </el-button
                >
                <div slot="tip" class="el-upload__tip">
                  {{ currentType === '0' ? '请上传 txt / word / pdf格式的文件' : '请上传 xls / xlsx 格式的文件' }}
                </div>
              </el-upload>
            </el-form-item>
          </div>
        </div>
        <div class="myDialog-footer">
          <el-button class="editConfirm" type="primary" @click="uploadKbFile"
          >确 定
          </el-button
          >
          <el-button class="editClose" @click="upload_file_state = false"
          >取 消
          </el-button
          >
        </div>
      </el-form>
    </el-dialog>
    <!-- 问答库 新建/编辑 -->
    <el-dialog :visible.sync="wendaCreateAndEditState" @close="reset" width="60%">
      <el-form
        :model="formKV"
        ref="formKV"
        :rules="rulesKV"
        label-width="80px"
        :inline="false"
        size="normal"
      >
        <div class="myDialog-body">
          <span class="myDialog-title">{{ titleWD }}问答库</span>
          <el-form-item label="用户问 :" prop="question" style="width: 80%;">
            <el-input size="medium " placeholder="请输入用户问问题" v-model.trim="formKV.question"/>
          </el-form-item>
          <!--          <el-form-item label="相似问 :" style="width: 80%;" class="similarityList">-->
          <!--            <div class="similarityQuestionItem" v-for="(item,index) in formKV.similarityQuestionList">-->
          <!--              <el-input size="medium " placeholder="请输入相似问问题" v-model.trim="item.similarityQuestion"/>-->
          <!--              <i class="el-icon-remove-outline" @click="removeSimilarityQuestion(index)"></i>-->
          <!--            </div>-->
          <!--            <i class="el-icon-circle-plus-outline" @click="addSimilarityQuestion"></i>-->
          <!--          </el-form-item>-->
          <el-form-item label="回答" style="width: 80%;" prop="answer">
            <el-input size="medium " type="textarea" placeholder="请输入相似问回答" v-model.trim="formKV.answer"/>
          </el-form-item>
        </div>
        <div class="myDialog-footer">
          <el-button class="editConfirm" type="primary" @click="uploadKV"
          >确 定
          </el-button
          >
          <el-button class="editClose" @click="wendaCreateAndEditState = false"
          >取 消
          </el-button
          >
        </div>
      </el-form>
    </el-dialog>
    <!-- 问答库文件上传记录 -->
    <el-dialog :visible.sync="uploadRecordState" width="60%">
      <div class="myDialog-body">
        <span class="myDialog-title">上传记录</span>
        <div style="height: 470px;">
          <el-table
            :data="fileRecordData"
            style="width: auto"
            height="490"
          >
            <el-table-column
              prop="createBy"
              label="上传者"
              align="center"
              width="120">
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="上传日期"
              align="center"
              width="180">
            </el-table-column>
            <el-table-column
              prop="fileName"
              label="文件名称"
              align="center"
              width="180">
            </el-table-column>
            <el-table-column
              prop="fileSize"
              label="文件大小"
              align="center"
              width="120">
              <template slot-scope="scope">
                {{ scope.row.fileSize | filterFileSizeKB }}
              </template>
            </el-table-column>
            <el-table-column
              prop="employeeName"
              label="员工名称"
              align="center"
              width="150">
              {{ this.employeeName }}
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {add, createWD, del, deleteFileDQ, deleteWD, delFile, fileListDQ, list, listWD, loadingFileRecordAll, saveFile, selectSimilarQuestionList, selectWD, showFace, update, updateWD, uploadFileDQ, uploadFileWD} from "@/api/kb/kb.js";
import {get} from "@/api/employee/employee"
import {uuid} from "vue-uuid";

export default {
  data() {
    return {
      baseWsUrl:process.env.VUE_APP_BASE_WEBSOCKET_URL,
      stateOptions: [
        {
          id: "",
          state: "全部",
        },
        {
          id: 0,
          state: "待训练",
        },
        {
          id: 1,
          state: "已完成",
        },
        {
          id: 2,
          state: "训练中",
        },
        {
          id: 3,
          state: "异常",
        },
      ],
      wendaStateOptions: [
        {
          id: "",
          state: "全部",
        },
        {
          id: 0,
          state: "批量上传"
        },
        {
          id: 1,
          state: "新建问题"
        },
      ],
      sameProblemStateShow: false,
      zsk: [],
      delete_bk_state: false,
      delete_not_bk_state: false,
      currentType: "0",
      create_kb_state: false,
      edit_kb_state: false,
      del_file_state: false,
      upload_file_state: false,
      titleWD: "新建",
      // 用来存放创建知识库的数据
      form: {
        name: "",
        collectionName: "",
        notes: "",
        fileNum: 0,
        fileList: [],
      },
      // 校验知识库名称是否输入
      rules: {
        name: [
          {required: true, message: "请输入知识库名称", trigger: "blur"},
        ],
      },
      // 校验问答库form
      rulesKV: {
        question: [
          {required: true, message: "请输入用户问问题!", trigger: "blur"}
        ],
        answer: [
          {required: true, message: "请输入用户问回答!", trigger: "blur"}
        ]
      },
      // 问答库form表单
      formKV: {
        question: "",
        // similarityQuestionList: [{similarityQuestion: ""}],
        answer: "",
        digitalEmployeeId: this.$route.query.id
      },
      loading: false,
      tableData: [],
      tableDataWD: [],
      total: 0,
      totalWD: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        fileName: "",
        status: "",
        dateRangeSendTime: [],
        digitalEmployeeId: this.$route.query.id
      },
      wendaQueryParams: {
        pageNum: 1,
        pageSize: 12,
        question: "",
        createType: "",
        dateRangeSendTime: [],
        digitalEmployeeId: this.$route.query.id
      },
      // 当前知识库id
      currentKb: null,
      // 删除的知识库id
      selectDeleteKbId: null,
      // 修改知识库的id
      editKbId: null,
      // 要删除的文件id
      delFileId: null,
      // 文档多选框选中数组
      idsDQ: [],
      // 问答多选框选中数组
      idsWD: [],
      // 文档非多个禁用
      multipleDQ: true,
      // 问答非多个禁用
      multipleWD: true,
      //  问答库 新建/编辑状态
      wendaCreateAndEditState: false,
      //   上传记录
      uploadRecordState: false,
      //   当前智能客服
      employeeId: "",
      // 当前智能客服名称
      employeeName: "",
      // 问答库 历史记录
      fileRecordData: [],
      //  上传文件对话框标题
      uploadTitle: "文档库",
      //   websocket连接池
      wsList: [],
    };
  },
  filters: {
    filterFileSizeKB(val) {
      if (val < 1024) {
        return val + "字节"
      }
      if (val % 1024 == 0) {
        return val / 1024 + "KB";
      } else {
        return (val / 1024).toFixed(2) + "KB";
      }
    },
    filterTrainingStatus(val) {
      switch (val) {
        case 0:
          return "待训练";
        case 1:
          return "已完成";
        case 2:
          return "训练中";
        case 3:
          return "异常";
      }
    },
    //   过滤操作按钮状态
    filterOperate(val) {
      if (val == 0) {
        return "训练"
      } else if (val == 1 || val == 3) {
        return "重新训练"
      }
      return "重新训练"
    },
    //   过滤 问答库 文件来源
    filterFileSource(val) {
      if (val == 0) {
        return "批量上传"
      } else if (val == 1) {
        return "新建问题"
      }
    }
  },
  mounted() {
    //console.log(window.cfg.xxx)
    this.employeeId = this.$route.query.id
    // 获取当前智能客服名称
    get(this.employeeId).then(res => {
      if (res.code == 200) {
        this.employeeName = res.data.name;
      }
    })
    //   获取问答库 问答列表
    this.loadingListWD()
    //   获取文档库 文件列表
    this.loadingListDQ()
  },
  beforeDestroy() {
    this.handleResultClose()
  },
  methods: {
    initWebSocket(fileId) {
      if (typeof WebSocket === "undefined") {
        alert("您的浏览器不支持WebSocket");
        return false;
      }
      let ip = this.baseWsUrl
      if (ip.startsWith("http:")) {
        ip = ip.substr(7)
      } else if (ip.startsWith("https:")) {
        ip = ip.substr(8)
      }
      const wsuri = `ws://${ip}/bizWebSocket/${fileId}` // websocket地址
      //console.log(wsuri)
      const websocket = {
        websocketItem: null,
        id: null,
      }
      websocket.id = fileId
      websocket.websocketItem = new WebSocket(wsuri);
      websocket.websocketItem.onopen = this.websocketonopen(websocket);
      websocket.websocketItem.onmessage = this.websocketonmessage;
      websocket.websocketItem.onerror = this.websocketonerror;
      websocket.websocketItem.onclose = this.websocketclose;
    },
    websocketonopen(websocket) {
      //console.log("链接成功!", websocket)
      this.upload_file_state = false
      // 连接成功把连接添加到连接池中
      this.wsList.push(websocket)
      // 发送请求远程保存文件（训练）
      saveFile({knowledgeFileId: websocket.id}).then(res => {
        if (res.code == 200) {
          this.$message({
            message: "操作成功！",
            type: "success",
          });
          this.resetFileList();
          this.upload_file_state = false
        }
      }).catch((err) => {
        this.loadingListDQ();
        this.upload_file_state = false
      })
      //console.log("websocket连接池--->", this.wsList)
    },
    websocketonerror(e) {
      //console.log(`连接失败的信息：`, e);
    },
    //接收后端返回的数据，可以根据需要进行处理
    websocketonmessage(e) {
      //console.log("收到消息-->", e)
      // 更新列表
      //   收到消息如果为已完成的状态 关闭连接
      if (e.isTrusted) {
        const data = JSON.parse(e.data)
        // this.loadingListDQ();
        const table = JSON.parse(JSON.stringify(this.tableData));
        for (const item of table) {
          if (item.id == data.id) {
            //console.log("item--->", item, "item.status--->", item.status, "data.status", data.status);
            item.status = data.status
            break;
          }
        }
        this.tableData = table
        if (data.status == 1) {
          this.handleResultClose(data.id)
        }
      }
    },
    //关闭连接
    websocketclose(e) {
      //console.log("断开连接-->", e);
    },


    //页面销毁时关闭ws连接
    handleResultClose(id) {
      if (this.wsList != [] && this.wsList.length != 0) {
        if (id) {
          for (const ws of this.wsList) {
            if (ws.id == id) {
              ws.websocketItem.close()
              this.wsList.splice(this.wsList.indexOf(ws), 1)
              //console.log("断开指定连接-->", ws, ",当前连接池--->", this.wsList)
              break;
            }
          }
        } else {
          // 关闭所有连接
          //console.log("断开全部连接")
          for (const ws of this.wsList) {
            ws.websocketItem.close()
          }
          this.wsList = []
          //console.log("断开全部连接,当前连接池--->", this.wsList)
        }
      }

    },
    // 修改知识库
    edit_kb(id) {
      this.edit_kb_state = true;
      this.editKbId = id;
      showFace(id).then((res) => {
        if (res.code == 200) {
          this.form = res.data;
        }
      });
    },
    // 确认修改知识库
    confirmEditKb() {
      const obj = {
        ...this.form,
        collectionName: uuid.v1(),
      };
      update(obj).then((res) => {
        if (res.code == 200) {
          this.edit_kb_state = false;
          this.toolsSuccess();
          this.getKbList();
        } else {
          this.toolsWarning();
        }
      });
    },
    // 删除知识库
    delete_bk(id) {
      //console.log(id);
      // 先用id去查询 当前知识库内是否存在文件
      showFace(id).then((res) => {
        if (res.code == 200) {
          if (res.data.fileList.length != 0) {
            this.delete_not_bk_state = true;
          } else {
            this.selectDeleteKbId = id;
            this.delete_bk_state = true;
          }
        }
      });
    },
    // 确认删除知识库
    confirmDeleteKb() {
      //console.log("删除", this.selectDeleteKbId);
      del(this.selectDeleteKbId).then((res) => {
        if (res.code == 200) {
          this.toolsSuccess();
          this.getKbList();
        }
      });
      this.delete_bk_state = false;
    },
    // 获取知识库列表
    getKbList() {
      list().then((res) => {
        if (res.code == 200) {
          this.zsk = res.rows;
        }
      });
    },
    // 创建知识库
    createKB() {
      const obj = {
        ...this.form,
        collectionName: uuid.v1(),
      };
      add(obj).then((res) => {
        if (res.code == 200) {
          this.getKbList();
          this.create_kb_state = false;
        } else {
          this.toolsWarning();
        }
      });
    },
    // 表单重置
    reset() {
      this.titleWD = "修改";
      this.formKV = {
        question: "",
        similarityQuestionList: [{similarityQuestion: ""}],
        answer: "",
        digitalEmployeeId: this.employeeId
      }
    },
    // 提交文件
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 重置上传文件列表
    resetFileList() {
      this.form.fileList = []
    },
    // 修改 问答
    updateWDHandler(row) {
      this.titleWD = "修改";
      selectWD(row.id).then(res => {
        if (res.code == 200) {
          this.formKV = res.data;
          this.wendaCreateAndEditState = true
        }
      })
    },
    // 上传问答库 问题
    uploadKV() {
      this.$refs.formKV.validate((valid) => {

        if (valid) {
          // this.formKV.similarityQuestionList = this.formKV.similarityQuestionList.filter((item) => {
          //   return item.similarityQuestion != "";
          // })
          if (this.formKV.id) {
            this.titleWD = "修改";
            //   修改 问答
            updateWD(this.formKV).then(res => {
              if (res.code == 200) {
                this.$message({
                  message: "修改问答成功！",
                  type: "success",
                });
                this.loadingListWD()
                this.wendaCreateAndEditState = false;
              }
            })
          } else {
            this.titleWD = "新建";
            //   新建问答
            createWD(this.formKV).then((res) => {
              if (res.code == 200) {
                this.$message({
                  message: "新建问答成功！",
                  type: "success",
                });
                this.loadingListWD()
                this.wendaCreateAndEditState = false;
              }
            })
          }
        }
      })
    },
    // 文件改变时触发
    changeFile(file, fileList) {
      //console.log(file, fileList);
      this.form.fileNum = fileList.length;
      this.form.fileList = fileList;
      //console.log(this.form.fileList);
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection, type) {
      let flg = false;
      if (type == "DQ") {
        this.idsDQ = [];
        this.idsDQ = selection.map((item) => item.id);
        this.multipleDQ = !selection.length || flg;
        //console.log("ids", this.idsDQ);
      } else if (type == "WD") {
        this.idsWD = [];
        this.idsWD = selection.map((item) => item.id);
        this.multipleWD = !selection.length || flg;
        //console.log("ids", this.idsWD);
      }

    },
    /**文件列表翻页 */
    getList() {
    },
    // 获取知识库详情
    getKbFiles(id) {
      this.currentKb = id;
      showFace(id).then((res) => {
        if (res.code == 200) {
          //console.log(res);
          this.tableData = res.data.fileList;
        }
      });
    },
    // 删除知识库文件
    delKbFile(row) {
      const id = row.id || this.idsDQ
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        autofocus: false
      }).then(() => {
        deleteFileDQ(id).then(res => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            })
            this.loadingListDQ()
          }
        })
      })
    },
    // 确认删除知识库内的文件
    confirmDelKbFile() {
      const ids = this.delFileId ? this.delFileId : this.ids;
      delFile(ids).then((res) => {
        if (res.code == 200) {
          this.delFileId = null;
          this.getKbFiles(this.currentKb);
          this.del_file_state = false;
        }
      });
    },
    // 知识库文件上传
    uploadKbFile() {
      if (this.form.fileList.length == 0) {
        this.$message({
          message: "请选择要上传的文件！",
          type: "warning",
        });
        return;
      }
      if (this.currentType == 0) {
        const dqFormData = new FormData();
        dqFormData.append("files", this.form.fileList[0].raw);
        dqFormData.append("digitalEmployeeId", this.employeeId);
        uploadFileDQ(dqFormData).then(res => {
          if (res.code == 200) {
            const form = {
              knowledgeFileId: res.data.fileId
            }
            this.loadingListDQ().then(() => {
              // 打开websocket连接
              this.initWebSocket(form.knowledgeFileId);
            })
          }
        })
      } else {
        const wdFormData = new FormData();
        for (const item of this.form.fileList) {
          wdFormData.append("files", item.raw);
        }
        wdFormData.append("digitalEmployeeId", this.employeeId);
        uploadFileWD(wdFormData).then((res) => {
          if (res.code == 200) {
            // this.getKbFiles(this.currentKb);
            this.$message({
              message: "上传成功！",
              type: "success",
            });
            this.resetFileList();
            this.loadingListWD();
            this.upload_file_state = false
          }
        });
      }
    },
    // 操作成功弹窗
    toolsSuccess() {
      this.$message({
        message: "操作成功",
        type: "success",
      });
    },
    //警告
    toolsWarning() {
      this.$message({
        message: "知识库名称不可重复！",
        type: "warning",
      });
    },
    // 删除相似问输入框
    removeSimilarityQuestion(id) {
      this.formKV.similarityQuestionList.splice(id, 1);
    },
    //   添加相似问对话框
    addSimilarityQuestion() {
      this.formKV.similarityQuestionList.push({similarityQuestion: ""})
    },
    handleQueryKV() {

    },
    handleQuery() {

    },
    //   重置按钮
    resetQuery(type) {
      if (type == 'document') {
        this.queryParams = {
          pageNum: 1,
          pageSize: 12,
          fileName: "",
          status: "",
          dateRangeSendTime: [],
          digitalEmployeeId: this.employeeId
        }
      } else if (type == 'kv') {
        this.wendaQueryParams = {
          pageNum: 1,
          pageSize: 12,
          question: "",
          createType: "",
          dateRangeSendTime: [],
          digitalEmployeeId: this.employeeId
        }
        this.loadingListWD()

      }
    },
    //   超出文件数量警告
    exceedFileError() {
      this.$message({
        message: "一次最多上传10个文件",
        type: "warning",
      })
    },
    //   加载历史记录
    loadingFileRecord() {
      this.uploadRecordState = true
      const queryParams = {
        digitalEmployeeId: this.employeeId
      }
      loadingFileRecordAll(queryParams).then(res => {
        if (res.code == 200) {
          this.fileRecordData = res.rows
        }
      })
    },
    //   加载问答列表
    loadingListWD() {
      if (this.sameProblemStateShow) {
        selectSimilarQuestionList(addDataFun({...this.wendaQueryParams})).then(res => {
          if (res.code == 200) {
            this.totalWD = res.total;
            this.tableDataWD = res.rows
          }
        })
      } else {
        listWD(addDataFun({...this.wendaQueryParams})).then(res => {
          if (res.code == 200) {
            this.totalWD = res.total;
            this.tableDataWD = res.rows
          }
        })
      }

      // 拆分时间搜索的数据
      function addDataFun(obj) {
        if (obj.dateRangeSendTime && obj.dateRangeSendTime.length !== 0) {
          obj.startTime = obj.dateRangeSendTime[0]
          obj.endTime = obj.dateRangeSendTime[1]
        }
        return obj
      }
    },
    handleClick(tab, event) {
      this.currentType = tab.index;
    },
    //   删除问答项
    removeWD(row) {
      const id = row.id?[row.id]:this.idsWD;
      //console.log("id--->", id, this.idsWD)
      this.$confirm('此操作将永久删除该问答, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteWD({ids: id, digitalEmployeeId: this.wendaQueryParams.digitalEmployeeId}).then(res => {
          if (res.code == 200) {
            this.$message({
              message: "删除成功！",
              type: "success",
            })
            this.loadingListWD()
          }
        })
      })
    },
    //   文档库 文件列表
    loadingListDQ() {
      return new Promise((resolve, reject) => {
        //console.log("this.queryParams--->", this.queryParams)
        fileListDQ(addDataFun({...this.queryParams})).then(res => {
          if (res.code == 200) {
            this.total = res.total
            this.tableData = res.rows
            resolve()
          }
        })
      })

      function addDataFun(obj) {
        if (obj.dateRangeSendTime && obj.dateRangeSendTime.length !== 0) {
          obj.startDateTime = obj.dateRangeSendTime[0]
          obj.endDateTime = obj.dateRangeSendTime[1]
          obj.sss = "222"
        }
        return obj
      }
    },
    //   加载相同问题
    loadingSameProblem() {
      this.sameProblemStateShow = !this.sameProblemStateShow
      if (this.sameProblemStateShow) {
        //调取相同问接口
        selectSimilarQuestionList(addDataFun({...this.wendaQueryParams})).then(res => {
          if (res.code == 200) {
            this.totalWD = res.total;
            this.tableDataWD = res.rows
          }
        })

      } else {
        this.loadingListWD()
      }

      function addDataFun(obj) {
        if (obj.dateRangeSendTime && obj.dateRangeSendTime.length !== 0) {
          obj.startTime = obj.dateRangeSendTime[0]
          obj.endTime = obj.dateRangeSendTime[1]
        }
        return obj
      }

    },
  //   关闭当前窗口
    closeView(){
      this.$tab.closePage(this.$route).then(({visitedViews}) => {
        if (this.isActive(this.$route)) {
          ////console.log(visitedViews)
          this.toLastView(visitedViews, this.$route)
        }
      })
    },
    isActive(route) {
      return route.path === this.$route.path
    },
    toLastView(visitedViews, view) {
      const latestView = visitedViews.slice(-1)[0]
      if (latestView) {
        this.$router.push(latestView.fullPath)
      } else {
        // now the default is to redirect to the home page if there is no tags-view,
        // you can adjust it according to your needs.
        if (view.name === 'Dashboard') {
          // to reload home page
          this.$router.replace({path: '/redirect' + view.fullPath})
        } else {
          this.$router.push('/')
        }
      }
    },
  },
};
</script>
<style lang="scss">

//* {
//  padding: 0;
//  margin: 0;
//  box-sizing: border-box;
//}

.clearfix::after {
  content: "";
  clear: both;
  display: block;
}

.container {
  width: 100%;
  background: white;
  padding: 20px;
  overflow: auto;
  box-sizing: border-box;
  .closeBtn{
    width: 38px;
    height: 38px;
    border-radius: 10px;
    border: 1px solid #cccccc;
    position: absolute;
    top: 10px;
    right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    z-index: 999;
  }
  .closeBtn:hover{
    border-color: rgb(29 147 171);
    background-color: rgb(229,229,229);
  }
  .closeBtn:active{
    opacity: 0.6;
  }
  .employeeName{
    position: absolute;
    top: 20px;
    left: 50%;
    transform:translate(-50%);
  }

  .myDialog-body {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;

    .myDialog-title {
      font-size: 20px;
      font-family: "PingFang SC";
      color: #444444;
      margin-bottom: 20px;
    }

    .myDialog-content {
      width: 80%;
    }

    .similarityList {
      .similarityQuestionItem {
        display: flex;
        align-items: center;
      }

      i {
        font-size: 25px;
        margin: 5px;
        cursor: pointer;

        &:hover {
          color: rgb(29, 147, 171);
        }

        &:active {
          color: rgb(229, 246, 250);
        }
      }
    }
  }

  .myDialog-footer {
    display: flex;
    align-items: center;
    justify-content: space-evenly;

    .editConfirm {
      color: #1d93ab;
      background-color: #e7f8ff;
      border-color: #1d93ab;
    }

    .editConfirm:hover,
    .editConfirm:focus {
      background: #b6dcec;
      border-color: #117e93;
      color: #1d93ab;
    }

    .editClose {
      color: #1d93ab;
    }

    .editClose:hover,
    .editClose:focus {
      background: #b6dcec;
      border-color: #117e93;
      color: #1d93ab;
    }
  }
}

.kb el-input:focus {
  border-color: #1d93ab;
}

.active {
  color: #1d93ab;
}

.mb8 {
  .el-button--primary {
    background: rgba(29, 147, 171, 0.16) !important;
    color: #519ba9 !important;
    border-color: rgba(29, 147, 171, 0.35) !important;

    i {
      color: #1d93ab !important;
    }

    &:hover {
      background: rgba(29, 147, 171, 1) !important;
      border-color: rgba(29, 147, 171, 1) !important;
      color: white !important;

      i {
        color: white !important;
      }
    }

    &:active {
      background: #0e7a90 !important;
      border-color: #0e7a90 !important;
    }
  }

  .el-button--success {
    background: rgba(249, 190, 3, 0.16) !important;
    color: #f9be03 !important;
    border-color: rgba(249, 190, 3, 0.35) !important;

    i {
      color: #f9be03 !important;
    }

    &:hover {
      background: rgba(249, 190, 3, 1) !important;
      color: white !important;
      border-color: rgba(249, 190, 3, 1) !important;

      i {
        color: white !important;
      }
    }

    &:active {
      background: #e99d42 !important;
      border-color: #e99d42 !important;
    }
  }

  .el-button--success.is-disabled {
    background: rgba(249, 190, 3, 0.16) !important;
    color: rgba(249, 192, 3, 0.7) !important;
    border-color: rgba(249, 190, 3, 0.25) !important;

    i {
      color: rgba(249, 192, 3, 0.7) !important;
    }
  }

  .el-button--danger {
    background: rgba(240, 120, 116, 0.16) !important; //背景色 16%
    color: #f07974 !important;
    border-color: rgba(240, 120, 116, 0.35) !important;

    i {
      color: #f07874 !important;
    }

    &:hover {
      background: rgba(240, 120, 116, 1) !important; //背景色 16%
      color: white !important;
      border-color: rgba(240, 120, 116, 1) !important;

      i {
        color: white !important;
      }
    }

    &:hover {
      background: #d9635e !important;
      border-color: #d9635e !important;
    }
  }

  .el-button--danger.is-disabled {
    background: rgba(240, 120, 116, 0.16) !important; //背景色 16%
    color: rgba(240, 120, 116, 0.7) !important;
    border-color: rgba(240, 120, 116, 0.35) !important;

    i {
      color: rgba(240, 120, 116, 0.7) !important;
    }
  }
}

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

.el-input__inner:focus {
  border-color: #1d93ab !important;
}

.el-textarea__inner:focus {
  border-color: #1d93ab !important;
}

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

.el-select-dropdown__item:hover {
  background: #e7f8ff;
}

.selected {
  color: #1d93ab !important;
}

//.pagination-container {
//  height: 30px;
//}
</style>
