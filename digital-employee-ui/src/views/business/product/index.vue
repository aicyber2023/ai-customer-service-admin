<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="产品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入产品名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="当前状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择当前状态" clearable size="small">
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button
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
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDel"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
    </el-row>
    <el-table
      v-loading="loading"
      :data="tableData"
      default-expand-all
      @selection-change="handleSelectionChange"
      height="600px"
    >
      <el-table-column label="序号" width="55" align="center">
        <template slot-scope="scope">
          {{
            scope.$index + 1 + (queryParams.pageNum - 1) * queryParams.pageSize
          }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="产品名称" align="center" min-width="200"></el-table-column>
      <el-table-column prop="employeeAmount" label="数字员工个数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.employeeAmount + '个' }}
        </template>
      </el-table-column>
      <el-table-column prop="sessionAmount" label="总对话条数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.sessionAmount + '条' }}
        </template>
      </el-table-column>
      <el-table-column prop="knowledgeBaseAmount" label="知识库个数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.knowledgeBaseAmount + '个' }}
        </template>
      </el-table-column>
      <el-table-column prop="knowledgeBaseDocAmount" label="总上传文档数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.knowledgeBaseDocAmount + '个' }}
        </template>
      </el-table-column>
      <el-table-column prop="knowledgeBaseDocWordAmount" label="总字数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.knowledgeBaseDocWordAmount + '字' }}
        </template>
      </el-table-column>
      <el-table-column prop="priceShow" label="展示原价" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.priceShow + '元' }}
        </template>
      </el-table-column>
      <el-table-column prop="priceReal" label="实际单价" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.priceReal + '元' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="当前状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | filterCss(statusOptions)">
            {{ scope.row.status | filterStage(statusOptions) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" width="150"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-upload2"
            v-if="scope.row.status != 1"
            @click="handleChangeStatus(scope.row, 1)"
          >上架</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.status == 0"
            @click="handleDel(scope.row)"
          >删除</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            v-else-if="scope.row.status == 1"
            @click="handleChangeStatus(scope.row, 2)"
          >下架</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false"  width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="数字员工个数" prop="employeeAmount">
          <el-input-number :min="0" controls-position="right" v-model="form.employeeAmount" placeholder="请输入数字员工个数" />
          个
        </el-form-item>
        <el-form-item label="总对话条数" prop="sessionAmount">
          <el-input-number :min="0" controls-position="right" v-model="form.sessionAmount" placeholder="请输入总对话条数" />
          条
        </el-form-item>
        <el-form-item label="知识库个数" prop="knowledgeBaseAmount">
          <el-input-number :min="0" controls-position="right" v-model="form.knowledgeBaseAmount" placeholder="请输入知识库个数" />
          个
        </el-form-item>
        <el-form-item label="总上传文档数" prop="knowledgeBaseDocAmount">
          <el-input-number :min="0" controls-position="right" v-model="form.knowledgeBaseDocAmount" placeholder="请输入总上传文档数" />
          个
        </el-form-item>
        <el-form-item label="总字数" prop="knowledgeBaseDocWordAmount">
          <el-input-number :min="0" controls-position="right" v-model="form.knowledgeBaseDocWordAmount" placeholder="请输入总字数" />
          字
        </el-form-item>
        <el-form-item label="展示原价" prop="priceShow">
          <el-input-number :min="1" :max="10000" :controls="false" :precision="2" v-model="form.priceShow" placeholder="请输入展示原价" />
          元
        </el-form-item>
        <el-form-item label="实际单价" prop="priceReal">
          <el-input-number :min="1" :max="10000" :controls="false" :precision="2" v-model="form.priceReal" placeholder="请输入实际单价" />
          元
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="small">
            <el-radio v-for="dict in statusOptions" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, get, del, add, update } from '@/api/business/product'
import { listData } from "@/api/system/dict/data";
  export default {
    name: "index",
    data(){
      return{
        loading:false,
        tableData:[],
        total: 0,
        queryParams:{
          pageNum: 1,
          pageSize: 10,
          name: "",
          status: undefined
        },
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        statusOptions:[],
        showSearch:true,
        title:"新增产品",
        open:false,
        form:{
          id: undefined,
          name: "",
          employeeAmount: undefined,
          sessionAmount: undefined,
          knowledgeBaseAmount: undefined,
          knowledgeBaseDocAmount: undefined,
          knowledgeBaseDocWordAmount: undefined,
          priceShow: undefined,
          priceReal: undefined,
          status: "0"
        },
        rules:{
          name:[{required:true, message:"请输入产品名称", trigger:"blur"}],
          employeeAmount:[{ validator: this.getAmountValidator(), trigger:"blur"}],
          sessionAmount:[{ validator: this.getAmountValidator(), trigger:"blur"}],
          knowledgeBaseAmount:[{ validator: this.getAmountValidator(), trigger:"blur"}],
          knowledgeBaseDocAmount:[{ validator: this.getAmountValidator(), trigger:"blur"}],
          knowledgeBaseDocWordAmount:[{ validator: this.getAmountValidator(), trigger:"blur"}],
          priceShow:[{required:true, message:"请输入展示原价", trigger:"blur"}],
          priceReal:[{required:true, message:"请输入实际单价", trigger:"blur"}],
        }
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
    },
    methods:{
      getAmountValidator() {
        return (rule, value, callback) => {
          // console.log(this, value)
          if (!this.form.employeeAmount && !this.form.sessionAmount
            && !this.form.knowledgeBaseAmount && !this.form.knowledgeBaseDocAmount && !this.form.knowledgeBaseDocWordAmount) {
            callback(new Error('至少需要设置一项！'))
          } else {
            callback()
          }
        }
      },
      getList(){
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
        this.queryParams.name = ""
        this.queryParams.status = undefined
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      handleChangeStatus(row, status) {
        let form = {
          id: row.id,
          status: status
        }
        update(form).then(res=>{
          if(res.code == 200){
            this.$modal.msgSuccess("操作成功");
            this.getList();
          }
        })
      },
      submitForm(){
        this.$refs["form"].validate(valid => {
          if (valid) {
            let form = {
              name: this.form.name,
              employeeAmount: this.form.employeeAmount,
              sessionAmount: this.form.sessionAmount,
              knowledgeBaseAmount: this.form.knowledgeBaseAmount,
              knowledgeBaseDocAmount: this.form.knowledgeBaseDocAmount,
              knowledgeBaseDocWordAmount: this.form.knowledgeBaseDocWordAmount,
              priceShow: this.form.priceShow,
              priceReal: this.form.priceReal,
              status: this.form.status,
            }
            if(this.form.id){
              form.id = this.form.id
              update(form).then(res=>{
                if(res.code == 200){
                  this.$modal.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
                }
              })
            }else{
              add(form).then(res=>{
                if(res.code == 200){
                  this.$modal.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
                }
              })
            }
          }
        });
      },
      cancel(){
        this.open = false;
        this.reset();
      },
      handleAdd(){
        this.reset();
        this.open = true;
        this.title = "新增产品";
      },
      handleUpdate(row){
        this.reset();
        const id = row.id || this.ids;
        get(id).then(response => {
          this.form = response.data;
          this.form.status = this.form.status + ""
          this.open = true;
          this.title = "修改产品";
        });
      },
      handleDel(row){
        const ids = row.id || this.ids;
        this.$confirm('确定要删除选中的产品吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          del(ids).then(res=>{
            if(res.code == 200){
              this.$modal.msgSuccess("操作成功");
              this.open = false;
              this.getList();
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          name: "",
          employeeAmount: undefined,
          sessionAmount: undefined,
          knowledgeBaseAmount: undefined,
          knowledgeBaseDocAmount: undefined,
          knowledgeBaseDocWordAmount: undefined,
          priceShow: undefined,
          priceReal: undefined,
          status: "0"
        };
        this.resetForm("form");
      },
    },
    created() {
      // 检测类型
      listData({dictType:'biz_product_status'}).then(res=>{
        if(res.code == 200){
          this.statusOptions = res.rows
        }
      })
      this.getList()
    }
  }
</script>

<style lang="scss" scoped>
  .review-type{
    padding: 10px 20px;
    height: calc(100vh - 86px);
    overflow-y: auto;
  }
</style>
