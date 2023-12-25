<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="分类名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入分类名称"
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
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
        >删除</el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="tableData"
      default-expand-all
      @selection-change="handleSelectionChange"
      height="600px"
    >
      <el-table-column type="selection" width="55" align="center" :selectable="getSelectable" />
      <el-table-column prop="name" label="分类名称" align="center" min-width="200"></el-table-column>
      <el-table-column prop="status" label="当前状态" min-width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | filterCss(statusOptions)">
            {{ scope.row.status | filterStage(statusOptions) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" width="150"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            :disabled="!scope.row.status"
            @click="handleUpdate(scope.row)"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            :disabled="!scope.row.status"
            @click="handleDel(scope.row)"
          >删除</el-button
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
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="small">
            <el-radio v-for="dict in statusOptions" :label="dict.dictValue" v-if="dict.dictValue != 0">{{ dict.dictLabel }}</el-radio>
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
import { list, get, del, add, update } from '@/api/template/type'
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
        title:"新增模板分类",
        open:false,
        form:{
          id: undefined,
          name: "",
          status: "0"
        },
        rules:{
          name:[{required:true, message:"请输入产品名称", trigger:"blur"}],
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
      getSelectable(row, index) {
        if (row.status) {
          return true;
        }
        return false;
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
      submitForm(){
        this.$refs["form"].validate(valid => {
          if (valid) {
            let form = {
              name: this.form.name,
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
        this.title = "新增模板分类";
      },
      handleUpdate(row){
        this.reset();
        const id = row.id || this.ids;
        get(id).then(response => {
          this.form = response.data;
          this.form.status = this.form.status + ""
          this.open = true;
          this.title = "修改模板分类";
        });
      },
      handleDel(row){
        const ids = row.id || this.ids;
        this.$confirm('确定要删除选中的模板分类吗?', '提示', {
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
          status: "1"
        };
        this.resetForm("form");
      },
    },
    created() {
      // 检测类型
      listData({dictType:'biz_template_type_status'}).then(res=>{
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
