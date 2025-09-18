<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="margin-left: 0px" class="selectbox-root">
            <span class="selectbox-hint" style="">文章类型</span>
            <div class="selectbox-deliver" />
            <el-cascader placeholder="全部" class="selectbox-input" :options="Articleoptions" v-model="catagoryValue"
              clearable @change="onClickSearch">
            </el-cascader>
          </div>
          <div style="margin-left: 20px" class="selectbox-root">
            <span class="selectbox-hint" style="">文章状态</span>
          <div class="selectbox-deliver" />                           <!--   绑定下拉数据源-->
            <el-cascader placeholder="全部" class="selectbox-input" :options="articleStatus" clearable v-model="statusValue"
              @change="articleStatusSelected">
            </el-cascader>
          </div>
          <div style="margin-left: 20px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 110px">文章搜索</span>
            <div class="selectbox-deliver" />
            <el-input v-model="searchKeyWord" placeholder="输入名称" clearable size="medium" @clear="onClickSearch"
              @keyup.enter.native="onClickSearch" />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="onClickSearch">
            搜索
          </button>
        </div>
        <div>
          <button style="margin-top: 0px; margin-bottom: 20px; width: 103px" class="normal-white-btn" @click="onAdd">
            +添加文章
          </button>
          <button style="
              margin-top: 0px;
              margin-bottom: 20px;
              width: 103px;
              margin-left: 15px;
            " class="normal-white-btn" @click="sameForArtical">
            同步文章
          </button>
          <el-table :header-cell-style="{
            background: '#F2F5F7',
            border: '0px solid #DDDDDD',
            color: '#242B35',
            height: '64px',
          }" :show-header="true" :data="articals" :row-style="{ height: '64px' }" stripe style="width: 100%">
            <!-- <el-table-column label="checkbox" align="center" width="40">
              <template slot="header" slot-scope="{column}">
                  <el-checkbox v-model="column.checked" :indeterminate="indeterminateFlag" :checked="allchecked" label="" @change="updateAllSelected"></el-checkbox>
              </template>
              <template slot-scope="scope">
                  <el-checkbox  @change="signCheckChange" v-model="scope.row.checked"></el-checkbox>
              </template>
            </el-table-column> -->
            <el-table-column min-width="20"></el-table-column>
            <el-table-column label="序号" align="left" min-width="60">
              <template slot-scope="scope"><span>{{ getCurListNo(scope.$index) }}</span></template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="title" label="文章名称" min-width="200" />
            <el-table-column align="left" prop="categoryIdName" label="文章类型" min-width="80" />
            <el-table-column align="left" prop="author" label="作者" min-width="90" />
            <el-table-column align="left" prop="statusName" label="文章状态" sortable min-width="90" />
            <el-table-column align="left" prop="browseNum" label="浏览量" min-width="100" />
            <el-table-column align="left" prop="updatedTime" label="发布日期" sortable min-width="100" />
            <el-table-column label="操作" min-width="200" align="center">
              <template slot-scope="scope">
                <a class="list-blue-text" @click="goArticle(scope.row.url)">查看</a>
                <a class="list-green-text" @click="onEdit(scope.row)"
                  style="margin-left: 10px">编辑</a>
                <a style="margin-left: 10px" :class="publishStyleChange(scope.row.statusName)"
                  @click="onClickPublish(scope.row.id)">发布</a>
                <a style="margin-left: 10px" @click="onClickOffline(scope.row.id)"
                  :class="offlineStyleChange(scope.row.statusName)">下线</a><!---->
                <a style="margin-left: 10px" class="list-red-text" @click="onClickDelete(scope.row.id)">删除</a>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
        <div style="flex-grow: 1" />
        <a style="margin: auto" class="pageBox-total-num">共{{ total }}条</a>
        <el-pagination style="margin: auto" background @size-change="handleSizeChange"
          @current-change="handleCurrentChange" :current-page="current" :page-size="pageSize" :page-count="pageCount"
          layout="total, sizes, prev, pager, next, jumper" :total="total">
        </el-pagination>
      </div>
    </div>
<!-- 添加文章 -->
    <el-dialog :title="dialogText" :visible.sync="addArticleFormVisible" width="40%">
      <el-row>
        <el-col :span="5"><span class="table-text">文章名称:<span style="color: red">*</span></span></el-col>
        <el-col :span="18">
          <el-input v-model="addArticleForm.title" placeholder="请输入文章名称" autocomplete="off" size="medium"></el-input>
        </el-col>
        <div style="clear: both; height: 20px"></div>
        <el-col :span="5"><span class="table-text">文章类型:<span style="color: red">*</span></span></el-col>
        <el-col :span="18">
           <el-select v-model="addArticleForm.categoryId" placeholder="请选择文章类型" size="medium" style="width: 100%">
              <el-option v-for="(item, index) in Articleoptions" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-col>
        <div style="clear: both; height: 20px"></div>
        <el-col :span="5"><span class="table-text">作者:</span></el-col>
        <el-col :span="18">
            <el-input v-model="addArticleForm.author" placeholder="请输入作者名称" autocomplete="off" size="medium"></el-input>
        </el-col>
        <div style="clear: both; height: 20px"></div>
        <el-col :span="5"><span class="table-text">文档url:<span style="color: red">*</span></span></el-col>
        <el-col :span="18">
            <el-input v-model="addArticleForm.url" placeholder="请输入文档url" autocomplete="off" size="medium"></el-input>
        </el-col>
        <div style="clear: both; height: 20px"></div>
      </el-row>
      <div slot="footer" class="dialog-footer" align="rigth">
        <el-button @click="onCancel">取 消</el-button>
        <el-button @click="saveMothod" type="primary">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import * as artical from "@/api/article";
import selectDropDownBox from "@/components/selectbox/selectDropDownBox.vue";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { cursor } from "@/libs/element-table";
import { setListNo } from "@/libs/public";
import {getNewsStatusDict} from "@/config/dictHelper";

export default {
  name: "companyPackage",
  components: { selectDropDownBox },
  data() {
    return {
      dialogText:"添加文章",
      addArticleFormVisible:false,
      addArticleForm:{
        "author": "",
        "browseNum": 0,
        "categoryId": "",
        "content": "",
        "copyFrom": "",
        "createdTime": "",
        "creatorId": 0,
        "description": "",
        "html": "",
        "id": 0,
        "imageUrl": "",
        "status": "",
        "title": "",
        "type": "",
        "updatedId": 0,
        "updatedTime": "",
        "url": ""
      },

      indeterminateFlag: false, //表头复选框状态
      reRender: true, // 重新渲染列表使用
      allchecked: false,
      searchKeyWord: "",
      articals: [],
      total: 0,
      current: 0,
      pageCount: 1,
      pageSize: 10,
      addUrl: "",
      addToken: "",
      exchangesUrl:
        "https://carbonmsger.feishu.cn/drive/folder/fldcn66yo6D4OoXwZqEMHL6OQSg?from=space_persnoal_filelist",
      Articleoptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "0180000001",
          label: "行业动态",
        },
        {
          value: "0180000002",
          label: "行业知识库",
        },
        {
          value: "0180000004",
          label: "常见问题",
        },
        {
          value: "0180000003",
          label: "平台公告",
        },
      ],
      articleStatus: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "0260000001",
          label: "未发布",
        },
        {
          value: "0260000002",
          label: "已发布",
        },
        {
          value: "0260000003",
          label: "已下线",
        },
      ],
      catagoryValue: [], //当前要查询的文章类型
      statusValue: [], //当前要查询的文章状态
    };
  },
  methods: {
    cellStyle({ row, column, rowIndex, columnIndex }) {
      if (column.label == "编号" || column.label == "操作") {
        return "margin-left:0px;";
      } else {
        return "cursor:pointer;";
      }
    },
    getCurListNo(index) {
      let curNo = parseInt(index) + 1;
      let size = this.size || "10";
      let page = this.current - 1;
      let no = setListNo(page, size, curNo);
      return no ? no : 1;
    },
    //同步文章按钮
    sameForArtical() {
      let data = {
        asc: true,
        categoryId: this.catagoryValue[0],
        searchKeywords: this.searchKeyWord,
        size: this.pageSize,
        sortField: "",
        status: this.statusValue[0],
      };

      // 只有当不是"全部"时才添加 categoryId 参数
      if (this.catagoryValue && this.catagoryValue[0] && this.catagoryValue[0] !== "0180000000") {
        data.categoryId = this.catagoryValue[0];
      }

      // 只有当不是"全部"时才添加 status 参数
      if (this.statusValue && this.statusValue[0] && this.statusValue[0] !== "0260000000") {
          data.status = this.statusValue[0];  // 其他情况保持原值
      }

      artical
        .syncArticle(data)
        .then((res) => {
          this.$message.success("操作成功");
          this.loadArticels();
        })
        .catch((reason) => {
          this.$message.error("连接超时，请重新连接");
        });
      // this.$message.success("该功能尚未实现，敬请期待");
    },
    /*
     *@Description: 添加文章
     *@MethodAuthor: liuboting
     *@Date: 2022-05-22 22:08:01
     */
    onAdd() {
       this.addArticleForm = {
        "author": "",
        "browseNum": 0,
        "categoryId": "",
        "content": "",
        "copyFrom": "",
        "createdTime": "",
        "creatorId": "",
        "description": "",
        "html": "",
        "imageUrl": "",
        "status": "",
        "title": "",
        "type": "",
        "updatedId": 0,
        "updatedTime": "",
        "url": ""
      },
      this.dialogText = "添加文章";
      this.addArticleFormVisible = true;
      // this.$message({
      //   type: "success",
      //   duration: 5000,
      //   message: "正在连接飞书...",
      // });
      // artical.AddArticle().then((res) => {
      //     this.addToken = res.token;
      //     this.addUrl = res.url;
      //     if (this.addUrl) {
      //       openUrlInNewWindow(this.addUrl);
      //     }
      //   })
      //   .catch((reason) => {
      //     this.$message.error("连接超时，请重新连接");
      //   });
    },
    onCancel(){
      this.addArticleFormVisible = false;
    },
    saveMothod(){
      let data = this.addArticleForm;
      if(!(data.author && data.url && data.title && data.categoryId)){
        return this.$message.error("必填项不能为空!")
      }
      // console.log('data111111111111',data.id);

      if(data.id){//调用修改接口
        for (let i in data) {
          if(data[i] == "--"){
            data[i]="";
          }
        }

        artical.updateArticle(data).then(res=>{
          this.$message.success("操作成功!")
          this.loadArticels();
        }).catch(res=>{
          this.$message.error(err.msg)
        })
      }else{
        delete data["id"]
        artical.addArticleToSql(data).then(res=>{
          this.$message.success("操作成功!")
          this.loadArticels();
        }).catch(res=>{
          this.$message.error(err.msg)
        })

      }

      this.addArticleFormVisible = false;
    },
    // cellStyle(data){
    //   return "cursor:pointer;"
    // },
    handle(row, column) {
      if (column.label != "操作") {
        openUrlInNewWindow(row.url);
      }
    },
    //判断是否发布，若发布了则修改样式
    editStyleChange(status) {
      if (status === "已发布") {
        return "afterSubmitEdit";
      } else {
        return "list-green-text";
      }
    },
    publishStyleChange(status) {
      if (status === "已发布") {
        return "afterSubmitPublish";
      } else {
        return "list-blue-text";
      }
    },
    offlineStyleChange(status) {
      if (status === "已发布") {
        return "list-red-text";
      } else {
        return "afterSubmitOffline";
      }
    },
    //当前选择的文章类型
    articalCatagorySelected() {
      const data = {
        asc: true,
        categoryId: this.catagoryValue[0],
        current: 0,
        size: 0,
        sortField:  this.statusValue[0],
      };
      // 只有当不是"全部"时才添加 categoryId 参数
      if (this.catagoryValue && this.catagoryValue[0] && this.catagoryValue[0] !== "0180000000") {
        data.categoryId = this.catagoryValue[0];
      }
      //
      // 只有当不是"全部"时才添加 status 参数
      if (this.statusValue && this.statusValue[0] && this.statusValue[0] !== "0260000000") {
        data.categoryId = this.statusValue[0];
      }
      artical
        .getCarbonArticles(data)
        .then((res) => {
          this.articals = res.records;
          this.total = Number(res.total);
          this.current = Number(res.current);
          this.pageCount = Math.ceil(parseInt(res.total) / this.pageSize);
          this.articals.map((v) => {
            //遍历表格数据
            v.checked = false;
            v.author = v.author ? v.author : "--";
            let time = v.updatedTime.split(" ");
            v.updatedTime = time[0];
          });
        })
        .catch((errror) => { });
    },
    //当前选择的文章状态
    articleStatusSelected(value) {
      this.statusValue = value;
      // 当状态改变时自动触发搜索
      this.onClickSearch();
    },
    // 格式化状态字典
    // formatStatus(data) {
    //   data.map((v) => {
    //     let CertificationItem = {
    //       label: "",
    //       value: "",
    //     };
    //     if (v.name == "全部") {
    //       CertificationItem.label = v.name;
    //       CertificationItem.value = "";
    //     } else {
    //       CertificationItem.value = v.value;
    //       CertificationItem.label = v.name;
    //     }
    //     this.accountStatusDict.push(CertificationItem);
    //     this.accountStatusDict[0].value='';
    //   });
    // },
     formatStatus(data){
      data.map((v) => {
        let acconuntItem={
          label:'',
          value:''
        };
          acconuntItem.label=v.name;
          acconuntItem.value=v.value;
        this.articleStatus.push(acconuntItem);
        this.articleStatus[0].value='';
      });
     },
    onClickSearch() {
      const data = {
        asc: true,
        categoryId: this.catagoryValue[0],
        current: 0,
        searchKeywords: this.searchKeyWord,
        size: 0,
        sortField: "",
        status: this.statusValue[0],
      };

      // 只有当不是"全部"时才添加 categoryId 参数
      if (this.catagoryValue && this.catagoryValue[0] && this.catagoryValue[0] !== "0180000000") {
        data.categoryId = this.catagoryValue[0];
      }

      // 只有当不是"全部"时才添加 status 参数
      if (this.statusValue && this.statusValue[0] && this.statusValue[0] !== "0260000000") {
        data.status = this.statusValue[0];
      }

      artical
        .getCarbonArticles(data)
        .then((res) => {
          console.log(res);
          this.articals = res.records;
          this.total = Number(res.total);
          this.current = Number(res.current);
          this.pageCount = Math.ceil(parseInt(res.total) / this.pageSize);
          this.articals.map((v) => {
            //遍历表格数据
            v.checked = false;
            // v.categoryName = this.categoryName(v.categoryId);
            // v.status = this.articleStatusSelect(v.status);
            let time = v.updatedTime.split(" ");
            v.updatedTime = time[0];
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
              if (v[i] == " ") {
                v[i] = "--";
              }
            }
          });
        })
        .catch((errror) => { });
    },
    goArticle(url){
      openUrlInNewWindow(url);
    },
    onEdit(row) {
      this.dialogText = "编辑文章";
      this.addArticleFormVisible = true
      this.addArticleForm = {...row};
    },
    //发布碳文章
    onClickPublish(id) {
      const data = {
        id: id,
        status: "0260000002",
      };
      artical.changeCarbonStatusById(data).then(
        (res) => {
          this.$message.success("发布成功");
          this.loadArticels();
        },
        (error) => {
          this.$message.success("发布失败");
        }
      );
    },
    onClickDelete(id) {
      var id = parseInt(id);
      this.$confirm("删除内容不可复原，请谨慎操作").then(() => {
        artical.DelArticle(id).then(
          (res) => {
            this.$message.success("删除成功");
            this.loadArticels();
          },
          (err) => {
            this.$message.success("删除失败");
          }
        );
      });
    },
    onClickOffline(id) {
      const data = {
        id: id,
        status: "0260000003",
      };
      artical.changeCarbonStatusById(data).then(
        (res) => {
          this.$message.success("下线成功");
          this.loadArticels();
        },
        (error) => {
          this.$message.success("下线失败");
        }
      );
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadArticels();
    },
    handleCurrentChange(val) {
      this.current = val;
      this.loadArticels();
    },
    // 监听页面宽度变化，刷新表格
    handleResize() {
      if (this.infoList) this.$refs.visitChart.handleResize();
    },
    // 加载碳文章列表
    loadArticels() {
      const data = {
        asc: true,
        current: this.current,
        size: this.pageSize,
        sortField: "",
      };
      artical
        .getCarbonArticles(data)
        .then((res) => {
          this.articals = res.records;
          this.total = Number(res.total);
          this.current = Number(res.current);
          this.pageCount = Math.ceil(parseInt(res.total) / this.pageSize);
          this.articals.map((v) => {
            //遍历表格数据
            v.checked = false;
            // v.categoryName = this.categoryName(v.categoryId);
            // v.status = this.articleStatusSelect(v.status);
            let time = v.updatedTime.split(" ");
            v.updatedTime = time[0];
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
              if (v[i] == " ") {
                v[i] = "--";
              }
            }
          });
        })
        .catch((errror) => { });
    },
    categoryName(id) {
      switch (id) {
        case 1:
          return "行业资讯";
        case 2:
          return "行业知识";
        case 4:
          return "常见问题";
        case 3:
          return "公告";
      }
    },
    // articleStatusSelect(status) {
    //   switch (status) {
    //     case 1:
    //       return "未发布";
    //     case 2:
    //       return "已发布";
    //     case 3:
    //       return "已下线";
    //     case 4:
    //       return "编辑中";
    //     case 5:
    //       return "已推送";
    //   }
    // },
    // checkbox start
    signCheckChange() {
      let allCheckedFlag = true;
      let allReset = true;
      this.articals.forEach((item) => {
        if (item.checked == true) {
          allReset = false;
        } else {
          allCheckedFlag = false;
        }
      });
      if (allCheckedFlag || allReset) {
        this.indeterminateFlag = false;
        if (allCheckedFlag) {
          this.allchecked = true;
        } else {
          this.allchecked = false;
        }
      } else {
        this.indeterminateFlag = true;
      }
      this.reRender = !this.reRender;
    },
    updateAllSelected(val) {
      this.indeterminateFlag = false;
      if (val) {
        this.articals.forEach((item) => {
          item.checked = true;
        });
      } else {
        this.articals.forEach((item) => {
          item.checked = false;
        });
      }
    },
    //render-header方法
    renderCheckHeader(h, { column, $index }) {
      return h("span", {}, [
        h("el-checkbox", {
          props: {
            checked: this.allchecked,
            indeterminate: this.indeterminateFlag, //表头复选框状态
          },
          on: {
            change: this.updateAllSelected, // 选中事件
          },
        }),
      ]);
    },
    // checkbox end
  },
  created() {
    // this.handleChangeVisitType();
  },
  mounted() {
    this.loadArticels();
    this.formatStatus(getNewsStatusDict(this.$store));
  },
};
</script>
<style lang="scss" scoped>
.root {
  background: #f2f5f7;
}

.divBox {
  margin: 20px;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px #eaf0f3;
  border-radius: 8px;
}

.container {
  margin: 20px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

>>>.el-cascader .el-input .el-input__inner,
>>>.el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.acea-row {
  ::v-deep.el-avatar--small {
    width: 22px;
    height: 22px;
    line-height: 22px;
  }
}

.checkTime {
  ::v-deep.el-radio__input {
    display: none;
  }
}

.ivu-pl-8 {
  margin-left: 8px;
  font-size: 14px;
}

.dashboard-console-visit {
  ::v-deep.el-card__header {
    padding: 14px 20px !important;
  }

  ul {
    li {
      list-style-type: none;
      margin-top: 12px;
    }
  }
}

.ivu-mb {
  margin-bottom: 10px;
}

.newsImg {
  width: 30px;
  height: 30px;
  border-radius: 4px;
}
.require {
  color: red;
  font-size: 16px;
  position: relative;
  right: 20px;
}
.table-text {
  position: relative;
  top: 10px;
  margin-left: 10px
}
</style>
