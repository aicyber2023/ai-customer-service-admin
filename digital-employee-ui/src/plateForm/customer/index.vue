<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="update-log" style="height: 200px">
          <div slot="header" class="clearfix">
            <span>快捷发起</span>
          </div>
          <div class="text item" style="padding: 10px 20px">
            <el-row>
              <el-col :span="6" v-if="checkRoles() >= 3">
                <el-link :underline="false" style="color: #666666;" @click="gotoBatch">
                  <div style="width: 100%;text-align: center">
                    <i class="el-icon-document-checked" style="font-size: 80px"></i>
                    <br/>
                    <span style="font-weight: 400;font-size: 14px;">发起检测</span>
                  </div>
                </el-link>
              </el-col>
              <el-col :span="6">
                <el-link :underline="false" style="color: #666666;" @click="gotoItem">
                  <div style="width: 100%;text-align: center">
                    <i class="el-icon-document" style="font-size: 80px"></i>
                    <br/>
                    <span style="font-weight: 400;font-size: 14px;">检查项一览</span>
                  </div>
                </el-link>
              </el-col>
              <el-col :span="6" v-if="checkRoles() >= 3">
                <el-link :underline="false" style="color: #666666;" @click="gotoCompany">
                  <div style="width: 100%;text-align: center">
                    <i class="el-icon-office-building" style="font-size: 80px"></i>
                    <br/>
                    <span style="font-weight: 400;font-size: 14px;">企业一览</span>
                  </div>
                </el-link>
              </el-col>
              <el-col :span="6">
                <el-link :underline="false" style="color: #666666;" @click="gotoOfficer">
                  <div style="width: 100%;text-align: center">
                    <i class="el-icon-user" style="font-size: 80px"></i>
                    <br/>
                    <span style="font-weight: 400;font-size: 14px;">安全员一览</span>
                  </div>
                </el-link>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="update-log" style="height: 200px">
          <div slot="header" class="clearfix">
            <span>通知公告</span>
          </div>
          <div class="text item" style="padding: 5px 20px;" v-for="item in noticeList">
            <el-link :underline="false" style="font-weight: 400;font-size: 12px;color: #999999;" @click="handleShowAll">
              <span style="width:500px;text-align:left;display:inline-block;">{{ item.noticeTitle }}</span>
              <i class="el-icon-timer"></i>
              <span style="text-align:left;display:inline-block;">{{ item.createTime }}</span>
            </el-link>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px" :gutter="20">
      <el-col :span="24">
        <el-card class="update-log" style="min-height: 400px">
          <div slot="header" class="clearfix">
            <span>检测记录</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item v-for="item in checkList" :title="item.batchName">
              <ol>
                <li>完成状态：{{ item.status | filterStage(statusOptions) }}</li>
                <li>参检企业：{{ item.statistic.reportTotalAmount }}家</li>
                <li>符合安全规定的企业：{{ item.statistic.approvedAmount }}家</li>
                <li>不符合安全规定的企业：{{ item.statistic.unApprovedAmount }}家</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {list} from '@/api/check/batch'
import {listData} from "@/api/system/dict/data";
import {getInfo} from "@/api/login";

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      noticeList: [],
      totalNotice: [],
      checkList: [],
      totalCheck: [],
      userInfo: {},
      version: "3.8.5"
    };
  },
  filters: {
    filterStage: function (val, list) {
      if (list && list.length != 0) {
        return list.filter(v => v.dictValue == val)[0].dictLabel;
      }
      return val;
    },
    filterCss: function (val, list) {
      if (list && list.length != 0) {
        return list.filter(v => v.dictValue == val)[0].listClass;
      }
      return val;
    },
  },
  created() {
    getInfo().then(res => {
      this.userInfo = res.user
    })
    // 检测记录状态
    listData({dictType: 'chk_batch_status'}).then(res => {
      if (res.code == 200) {
        this.statusOptions = res.rows
      }
    })
    // this.getListNotice();
    this.getListCheck();
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    // getListNotice() {
    //   let param = {
    //     pageNum:1,
    //     pageSize:5,
    //   }
    //   listNotice(param).then(res=>{
    //     if(res.code == 200){
    //       this.noticeList = res.rows
    //       this.totalNotice = res.total
    //     }
    //   })
    // },
    getListCheck() {
      let param = {
        pageNum: 1,
        pageSize: 5,
      }
      list(param).then(res => {
        if (res.code == 200) {
          this.checkList = res.rows
          this.totalCheck = res.total
        }
      })
    },
    checkRoles() {
      let flg = 0;
      this.userInfo.roles.forEach(role => {
        if (role.roleKey == 'admin') {
          flg = 9;
        } else if (role.roleKey == 'subAdmin') {
          flg = flg > 3 ? flg : 3;
        } else if (role.roleKey == 'other') {
          flg = flg > 2 ? flg : 2;
        } else if (role.roleKey == 'company') {
          flg = flg > 1 ? flg : 1;
        }
      })
      return flg;
    },
    gotoBatch() {
      this.$router.push('/check/batch')
    },
    gotoItem() {
      this.$router.push('/check/checkPoint')
    },
    gotoCompany() {
      this.$router.push('/system/dept')
    },
    gotoOfficer() {
      this.$router.push('/doc/safetyOfficer')
    },
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

