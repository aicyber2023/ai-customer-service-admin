<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="订单时间" prop="createTime">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          size="small"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select v-model="queryParams.productId" placeholder="请选择产品" clearable size="small">
          <el-option
            v-for="dict in productOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="订单金额">
        <el-col :span="10">
          <el-form-item prop="amountStart" style="width: 80px">
            <el-input v-model="queryParams.amountStart" size="small" />
          </el-form-item>
        </el-col>
        <el-col class="line" :span="4" style="text-align:center;">-</el-col>
        <el-col :span="10">
          <el-form-item prop="amountEnd" style="width: 80px">
            <el-input v-model="queryParams.amountEnd" size="small" />
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择订单状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="订单号" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入订单号"
          clearable
          size="small"
        />
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
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" plain
        >导出</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      v-loading="loading"
      :data="tableData"
      default-expand-all
      height="550px"
    >
      <el-table-column label="序号" width="55" align="center">
        <template slot-scope="scope">
          {{
            scope.$index + 1 + (queryParams.pageNum - 1) * queryParams.pageSize
          }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="生成时间" align="center" width="200"></el-table-column>
      <el-table-column prop="userId" label="用户ID" align="center" width="150"></el-table-column>
      <el-table-column label="用户昵称" align="center" min-width="150">
        <template slot-scope="scope">
          {{ scope.row.user ? scope.row.user.nickname : "" }}
        </template>
      </el-table-column>
      <el-table-column label="产品名称" align="center" width="120">
        <template slot-scope="scope">
          {{ scope.row.productId | filterProduct(productOptions) }}
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="订单金额（元）" align="center" width="150"></el-table-column>
      <el-table-column prop="payType" label="支付方式" align="center" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.payType == 0">微信支付</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center" width="120">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | filterCss(statusOptions)">
            {{ scope.row.status | filterStage(statusOptions) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="订单号" align="center" min-width="200"></el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { list, get, del, add, update, exportCsv } from '@/api/business/order'
import { listData } from "@/api/system/dict/data"
import { listAll } from "@/api/business/product";
export default {
  name: "index",
  data(){
    return{
      loading: false,
      tableData: [],
      total: 0,
      productOptions: [],
      statusOptions: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: "",
        productId: "",
        amountStart: undefined,
        amountEnd: undefined,
        status: undefined,
        id: "",
        createTimeStart: undefined,
        createTimeEnd: undefined
      },
      showSearch: true,
      openDetail: false,
    }
  },
  filters: {
    filterStage: function(val, list) {
      if (list && list.length != 0) {
        return list.filter(v=>v.dictValue == val)[0].dictLabel;
      }
      return val;
    },
    filterCss: function(val, list) {
      if (list && list.length != 0) {
        return list.filter(v=>v.dictValue == val)[0].listClass;
      }
      return val;
    },
    filterProduct: function(val, list) {
      if (list && list.length != 0) {
        return list.filter(v=>v.id == val)[0].name;
      }
      return val;
    },
  },
  methods:{
    getList() {
      if (this.dateRange && this.dateRange.length > 0) {
        this.queryParams.createTimeStart = this.dateRange[0]
        this.queryParams.createTimeEnd = this.dateRange[1]
      }
      list(this.queryParams).then(res=>{
        if(res.code == 200){
          this.tableData = res.rows
          this.total = res.total
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.userId = ""
      this.queryParams.productId = ""
      this.queryParams.amountStart = undefined
      this.queryParams.amountEnd = undefined
      this.queryParams.status = undefined
      this.queryParams.id = ""
      this.queryParams.createTimeStart = undefined
      this.queryParams.createTimeEnd = undefined
      this.dateRange = []
      this.resetForm("queryForm");
      this.handleQuery();
    },
    format(percentage) {
      return `${Math.round(percentage)}%`;
    },
    handleExport() {
      if (this.dateRange && this.dateRange.length > 0) {
        this.queryParams.createTimeStart = this.dateRange[0]
        this.queryParams.createTimeEnd = this.dateRange[1]
      }
      exportCsv(this.queryParams).then(res=>{
        const content = res
        const blob = new Blob([content])
        let fileName = '订单导出.csv'
        if ('download' in document.createElement('a')) {
          const elink = document.createElement('a')
          elink.download = fileName
          elink.style.display = 'none'
          elink.href = URL.createObjectURL(blob)
          document.body.appendChild(elink)
          elink.click()
          URL.revokeObjectURL(elink.href)
          document.body.removeChild(elink)
        } else {
          navigator.msSaveBlob(blob, fileName)
        }
      })
    }
  },
  created() {
    // 产品列表
    listAll().then(res=>{
      if(res.code == 200){
        this.productOptions = res.data
      }
    })
    // 订单状态
    listData({dictType:'biz_order_status'}).then(res=>{
      if(res.code == 200){
        this.statusOptions = res.rows
      }
    })
    this.getList()
  }
}
</script>

<style lang="scss" scoped>
.review-type {
  padding: 10px 20px;
  height: calc(100vh - 86px);
  overflow-y: auto;
}
</style>
