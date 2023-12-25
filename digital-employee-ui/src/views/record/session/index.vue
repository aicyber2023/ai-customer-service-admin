<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="客户" prop="sysUserId" v-if="showSysUser">
        <el-select
          v-model="queryParams.sysUserId"
          placeholder="请选择客户"
          size="small"
          clearable
        >
          <el-option
            v-for="item in userOptions"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="服务对象ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="数字员工" prop="digitalEmployeeId">
        <el-select
          v-model="queryParams.digitalEmployeeId"
          placeholder="请选择数字员工"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in employeeOptions"
            :key="dict.id"
            :label="dict.title"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间" prop="sendTime">
        <el-date-picker
          v-model="dateRangeSendTime"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          size="small"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="响应间隔">
        <el-col :span="10">
          <el-form-item prop="responseIntervalStart" style="width: 80px">
            <el-input
              v-model="queryParams.responseIntervalStart"
              size="small"
            />
          </el-form-item>
        </el-col>
        <el-col class="line" :span="4" style="text-align: center">-</el-col>
        <el-col :span="10">
          <el-form-item prop="responseIntervalEnd" style="width: 80px">
            <el-input v-model="queryParams.responseIntervalEnd" size="small"/>
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item label="返回状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择返回状态"
          clearable
          size="small"
          @change="handleStatusChanged"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="错误码" prop="errorCode">
        <el-input
          v-model="queryParams.errorCode"
          placeholder="请输入错误码"
          clearable
          :disabled="errorCodeDisabled"
          size="small"
        />
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
      <el-table-column prop="sysUserId" label="客户ID" align="center" width="100">
        <!-- <template slot-scope="scope">
          {{ scope.row.sysUserId | filterUser(userOptions) }}
        </template> -->
      </el-table-column>
      <el-table-column
        prop="userId"
        label="服务对象ID"
        align="center"
        width="100"
      />
      <el-table-column label="服务对象昵称" align="center" width="120">
        <template slot-scope="scope">
          {{ scope.row.nickName ? scope.row.nickName : "" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="sendTime"
        label="发送时间"
        align="center"
        width="150"
      />
      <el-table-column
        prop="returnTime"
        label="返回时间"
        align="center"
        width="150"
      />
      <el-table-column
        prop="responseInterval"
        label="响应间隔（ms）"
        align="center"
        width="80"
      />
      <el-table-column
        prop="tokens"
        label="Token数"
        align="center"
        width="100"
      />
      <el-table-column prop="digitalEmployeeId" label="数字员工" width="100">
        <template slot-scope="scope">
          {{ scope.row.digitalEmployeeId | filterEmployee(employeeOptions) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="inputText"
        label="用户输入"
        min-width="100"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="outputText"
        label="机器人返回"
        min-width="100"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column prop="status" label="状态" width="150" align="center">
        <template slot-scope="scope">
          {{
            scope.row.status | filterStage(statusOptions, scope.row.errorCode)
          }}
        </template>
      </el-table-column> -->
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
import {list} from "@/api/record/session";
import {listData} from "@/api/system/dict/data";
import {listAll as listEmployees} from "@/api/employee/employee";
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
        pageSize: 10,
        sysUserId: undefined,
        userId: "",
        digitalEmployeeId: undefined,
        sendTimeStart: undefined,
        sendTimeEnd: undefined,
        responseIntervalStart: undefined,
        responseIntervalEnd: undefined,
        status: undefined,
        errorCode: undefined,
      },
      statusOptions: [],
      userOptions: [],
      employeeOptions: [],
      dateRangeSendTime: [],
      errorCodeDisabled: true,
      showSearch: true,
      showSysUser: false,
      showTotal: [], // 简介全部显示
      exchangeButton: [], // 展开/收起文字改变
      showExchangeButton: [], // 是否显示 展开/收起 文字
    };
  },
  filters: {
    filterStage: function (val, list, errorCode) {
      if (list && list.length != 0) {
        let tmp = errorCode && errorCode != 200 ? "（" + errorCode + "）" : "";
        return list.filter((v) => v.dictValue == val)[0].dictLabel + tmp;
      }
      return val;
    },
    filterEmployee: function (val, list) {
      console.log(val, list);
      if (val != null) {
        if (list && list.length != 0) {
          console.log(list.filter((v) => v.id == val));
          return list.filter((v) => v.id == val).length != 0 ? list.filter((v) => v.id == val)[0].name : "";
        }
      }
      return "";
    },
    filterUser: function (val, list) {
      if (list && list.length != 0) {
        return list.filter((v) => v.userId == val)[0].nickName;
      }
      return val;
    },
  },
  methods: {
    getList() {
      if (this.dateRangeSendTime && this.dateRangeSendTime.length > 0) {
        this.queryParams.sendTimeStart = this.dateRangeSendTime[0];
        this.queryParams.sendTimeEnd = this.dateRangeSendTime[1];
      }

      list(this.queryParams).then((res) => {
        if (res.code == 200) {
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
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        sysUserId: undefined,
        userId: "",
        digitalEmployeeId: undefined,
        sendTimeStart: undefined,
        sendTimeEnd: undefined,
        responseIntervalStart: undefined,
        responseIntervalEnd: undefined,
        status: undefined,
        errorCode: undefined,
      };
      this.dateRangeSendTime = [];
      if (!this.showSysUser) {
        this.queryParams.sysUserId = this.userInfo.userId;
      }
      this.resetForm("queryForm");
      this.handleQuery();
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
      this.getList();
    });
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
