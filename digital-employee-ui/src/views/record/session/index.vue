<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="会话类型" prop="sessionType">
        <el-select
          v-model="queryParams.sessionType"
          size="small"
        >
          <el-option label="全部" value=""/>
          <el-option label="测试" :value="0"/>
          <el-option label="匿名" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item label="访客IP" prop="ip">
        <el-input
          v-model="queryParams.ip"
          placeholder="请输入访客IP"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="数字员工昵称" prop="digitalEmployeeId">
        <el-select
          v-model="queryParams.digitalEmployeeId"
          clearable
          size="small"
        >
          <el-option
            label="全部"
            value=""
          />
          <el-option
            v-for="item in employeeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="对话条数:">
        <template style="display: flex;align-items: center">
          <el-input v-model="queryParams.recordAmountStart" style="width: 80px;"></el-input>
          <span> ~ </span>
          <el-input v-model="queryParams.recordAmountEnd" style="width: 80px;"></el-input>
          <span> 条 </span>
        </template>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="createTime"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          size="small"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="最近对话时间">
        <el-date-picker
          v-model="recentlyConversationTime"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          size="small"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="创建人ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入创始人ID" clearable/>
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
      @selection-change="handleSelectionChange"
      height="550px"
    >
      <el-table-column prop="index" label="序号" align="center">
        <template v-slot="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="sessionType" label="会话类型" align="center" min-width="100px">
        <template v-slot="scope">
          {{ scope.row.sessionType | filterSessionType }}
        </template>
      </el-table-column>
      <el-table-column prop="testUserName" label="用户账号" align="center" min-width="100px"/>
      <el-table-column prop="ip" label="访客IP" align="center" min-width="100px"/>
      <el-table-column prop="deName" label="数字客服昵称" align="center" min-width="100px"/>
      <el-table-column prop="createTime" label="创建时间" align="center" min-width="140px"/>
      <el-table-column prop="updateTime" label="最近对话时间" align="center" min-width="140px"/>
      <el-table-column prop="recordAmount" label="对话条数" align="center" min-width="100px"/>
      <el-table-column prop="userId" align="center" label="创建人ID" min-width="100px"/>
      <el-table-column label="操作" align="center" width="200">
        <template v-slot="scope">
          <el-button type="text" @click="dialogueDetails(scope.row)">查看详情</el-button>
          <el-button type="text" @click="dialogueDelete(scope.row)">删除会话</el-button>
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

<script>
import {delSessionRecord, list} from "@/api/record/session";
import {listData} from "@/api/system/dict/data";
import {listAll, listAll as listEmployees} from "@/api/employee/employee";
import {listAll as listUsers} from "@/api/system/user";
import {getInfo} from "@/api/login";

export default {
  name: "index",
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        sessionType: "",
        ip: "",
        digitalEmployeeId: "",
        recordAmountStart:"",
        recordAmountEnd:"",
        userId: "",

        createTimeStart: undefined,
        createTimeEnd: undefined,
        updateTimeStart: undefined,
        updateTimeEnd: undefined,
      },
      // 数字员工列表
      employeeList: [],
      statusOptions: [],
      userOptions: [],
      employeeOptions: [],
      createTime: [],
      recentlyConversationTime: [],
      errorCodeDisabled: true,
      showSearch: true,
      showSysUser: false,
      showTotal: [], // 简介全部显示
      exchangeButton: [], // 展开/收起文字改变
      showExchangeButton: [], // 是否显示 展开/收起 文字
    };
  },
  filters: {
    filterSessionType: function (val) {
      if (val == 1) return "匿名"
      return "测试"
    },
  },
  methods: {
    // 查看对话详情
    dialogueDetails(row) {
      const {id} = row
      this.$router.push({
        name: 'sessionDetails',
        query: {
          id: id
        }
      })
    },
    // 删除对话
    dialogueDelete(row) {
      this.$confirm('确定要删除当前对话?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSessionRecord(row.id).then(res => {
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getList()
          }
        })
      })
    },
    getList() {
      if (this.createTime && this.createTime.length > 0) {
        this.queryParams.createTimeStart = this.createTime[0];
        this.queryParams.createTimeEnd = this.createTime[1];
      }
      if (this.recentlyConversationTime&&this.recentlyConversationTime.length>0){
        this.queryParams.updateTimeStart = this.recentlyConversationTime[0];
        this.queryParams.updateTimeEnd = this.recentlyConversationTime[1];
      }
      list(this.queryParams).then((res) => {
        if (res.code === 200) {
          this.tableData = res.rows;
          this.total = res.total;
        }
      });
    },
    showTotalIntro(idx) {
      this.showTotal[idx] = !this.showTotal[idx];
      this.exchangeButton[idx] = !this.exchangeButton[idx];
    },
    handleStatusChanged(val) {
      if (!val || val == 0) {
        this.errorCodeDisabled = true;
      } else {
        this.errorCodeDisabled = false;
      }
    },
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
        pageSize: 10,
        sessionType: "",
        ip: "",
        digitalEmployeeId: "",
        recordAmountStart: "",
        recordAmountEnd:"",
        userId: "",
        createTimeStart: undefined,
        createTimeEnd: undefined,
        updateTimeStart: undefined,
        updateTimeEnd: undefined,

      };
      this.createTime = [];
      this.recentlyConversationTime = [];
      this.dateRangeSendTime = [];
      this.resetForm("queryForm");
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = [];
      let flg = false;
      selection.forEach((ele) => {
        this.ids.push(ele.id);
        if (ele.status != 1) {
          flg = true;
        }
      });
      this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length || flg;
    },
  },
  created() {
    // 获取数字员工列表
    listAll().then(res=>{
      this.employeeList=res.data
    })
    listEmployees().then((res) => {
      if (res.code == 200) {
        this.employeeOptions = res.data;
      }
    });
    listUsers().then((res) => {
      if (res.code == 200) {
        this.userOptions = res.data;
      }
    });
    // 检测类型
    listData({dictType: "biz_record_status"}).then((res) => {
      if (res.code == 200) {
        this.statusOptions = res.rows;
      }
    });
    getInfo().then((res) => {
      this.showSysUser = false;
      res.roles.forEach((ele) => {
        if (ele == "admin" || ele == "common") {
          this.showSysUser = true;
        }
      });
      this.userInfo = res.user;
      if (!this.showSysUser) {
        this.queryParams.sysUserId = res.user.userId;
      }
    });
    this.getList();
  },
};
</script>

<style lang="scss" scoped>
.review-type {
  padding: 10px 20px;
  height: calc(100vh - 86px);
  overflow-y: auto;
}
</style>
