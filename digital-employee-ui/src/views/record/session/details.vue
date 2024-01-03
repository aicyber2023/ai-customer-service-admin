<script>
import {getSessionRecord} from "@/api/record/session";

export default {
  data() {
    return {
      queryParams: {
        id:"",
        pageNum: 1,
        pageSize: 12,
        status: "",
        responseIntervalStart:"",
        responseIntervalEnd:"",
        keyword: "",
        sendTimeStart: undefined,
        sendTimeEnd: undefined,
      },
      sendTime: [],
      total: 0,
      loading: false,
      tableData: []
    }
  },
  filters:{
    filterStatus:function (val){
      if (val===0) return "错误返回"
      if (val===1) return "正常返回"
      // return "超时"
    },
    filterCharType:function (val){
      if (val===0) return "仅问答库"
      if (val===1) return "仅文档库"
      if (val===2) return "问答库和文档库"
    },
    filterHitSituation:function (val){
      if (val===0) return "问答库"
      if (val===1) return "文档库"
      if (val===2) return "未命中"
    }
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
      console.log(this.queryParams)
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 12,
        status: "",
        responseIntervalStart:"",
        responseIntervalEnd:"",
        keyword: "",
        sendTimeStart: undefined,
        sendTimeEnd: undefined,
      };
      this.sendTime = [];
      // this.resetForm("queryForm");
    },
    getList() {
      this.loading=true
      console.log(this.$route.query.id)
      const id=this.$route.query.id
      this.queryParams.id=id;
      if (this.sendTime&&this.sendTime.length>0){
        this.queryParams.sendTimeStart=this.sendTime[0]
        this.queryParams.sendTimeEnd=this.sendTime[1]
      }
      getSessionRecord(this.queryParams).then(res=>{
        if (res.code===200){
          this.tableData=res.rows
          this.total=res.total
          this.loading=false
        }
      })
    },
    kbOptimize(){

    },
  },
  mounted() {
    this.getList()
  },

}
</script>

<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
    >
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          size="small"
        >
          <el-option label="全部" value=""/>
          <el-option label="错误返回" :value="0"/>
          <el-option label="返回正常" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item label="响应间隔:" >
        <template style="display: flex;align-items: center">
          <el-input v-model="queryParams.responseIntervalStart" style="width: 80px;"></el-input>
          <span> ~ </span>
          <el-input v-model="queryParams.responseIntervalEnd" style="width: 80px;"></el-input>
          <span> 秒 </span>
        </template>
      </el-form-item>
      <el-form-item label="关键词" prop="keyword">
        <el-input v-model="queryParams.keyword" placeholder="请输入关键词" clearable></el-input>
      </el-form-item>
      <el-form-item label="发送时间">
        <el-date-picker
          v-model="sendTime"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          size="small"
        ></el-date-picker>
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
    <el-table
      v-loading="loading"
      :data="tableData"
      default-expand-all
      height="550px"
    >
      <el-table-column prop="sendTime" label="发送时间" align="center" min-width="150px"/>
      <el-table-column prop="returnTime" label="返回时间" align="center" min-width="150px"/>
      <el-table-column prop="responseInterval" label="响应间隔" align="center" min-width="100px"/>
      <el-table-column prop="tokens" label="Token数" align="center" min-width="100px"/>
      <el-table-column prop="inputText" label="访客" align="center" width="100px" :show-overflow-tooltip="true">
        <template v-slot="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.inputText" placement="top"/>
          {{scope.row.inputText}}
        </template>
      </el-table-column>
      <el-table-column prop="outputText" label="数字客服" align="center" width="100px" :show-overflow-tooltip="true">
        <template v-slot="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.outputText" placement="top"/>
          {{scope.row.outputText}}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center" min-width="100px">
        <template v-slot="scope">
          {{ scope.row.status | filterStatus }}
        </template>
      </el-table-column>
      <el-table-column prop="errorMessage" align="center" label="错误信息" width="120px" :show-overflow-tooltip="true">
        <template v-slot="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.errorMessage" placement="top"/>
          {{scope.row.errorMessage}}
        </template>
      </el-table-column>
      <el-table-column prop="chatType" label="知识库支持" align="center" min-width="100px">
        <template v-slot="scope">
          {{ scope.row.chatType | filterCharType }}
        </template>
      </el-table-column>
      <el-table-column prop="hitStatus" label="命中情况" align="center" min-width="100px">
        <template v-slot="scope">
          {{ scope.row.hitStatus | filterHitSituation }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template v-slot="scope">
          <el-button type="text" @click="kbOptimize(scope.row)">知识库优化</el-button>
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
  </div>
</template>

<style scoped lang="scss">
.review-type {
  padding: 10px 20px;
  height: calc(100vh - 86px);
  overflow-y: auto;
}
</style>
