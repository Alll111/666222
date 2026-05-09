<template>
  <div class="main-content">
    <!-- 闁告帗顨夐妴鍐┿亜?-->
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-row  :gutter="20" class="slt" :style="{justifyContent:contents.searchBoxPosition=='1'?'flex-start':contents.searchBoxPosition=='2'?'center':'flex-end'}">
                <el-form-item :label="contents.inputTitle == 1 ? '璐﹀彿' : ''">
                  <el-input v-if="contents.inputIcon == 1 && contents.inputIconPosition == 1" prefix-icon="el-icon-search" v-model="searchForm.zhanghao" placeholder="璐﹀彿" clearable></el-input>
                  <el-input v-if="contents.inputIcon == 1 && contents.inputIconPosition == 2" suffix-icon="el-icon-search" v-model="searchForm.zhanghao" placeholder="璐﹀彿" clearable></el-input>
                  <el-input v-if="contents.inputIcon == 0" v-model="searchForm.zhanghao" placeholder="璐﹀彿" clearable></el-input>
                </el-form-item>
                <el-form-item :label="contents.inputTitle == 1 ? '濮撳悕' : ''">
                  <el-input v-if="contents.inputIcon == 1 && contents.inputIconPosition == 1" prefix-icon="el-icon-search" v-model="searchForm.xingming" placeholder="濮撳悕" clearable></el-input>
                  <el-input v-if="contents.inputIcon == 1 && contents.inputIconPosition == 2" suffix-icon="el-icon-search" v-model="searchForm.xingming" placeholder="濮撳悕" clearable></el-input>
                  <el-input v-if="contents.inputIcon == 0" v-model="searchForm.xingming" placeholder="濮撳悕" clearable></el-input>
                </el-form-item>
          <el-form-item>
            <el-button v-if="contents.searchBtnIcon == 1 && contents.searchBtnIconPosition == 1" icon="el-icon-search" type="success" @click="search()">{{ contents.searchBtnFont == 1?'鏌ヨ':'' }}</el-button>
            <el-button v-if="contents.searchBtnIcon == 1 && contents.searchBtnIconPosition == 2" type="success" @click="search()">{{ contents.searchBtnFont == 1?'鏌ヨ':'' }}<i class="el-icon-search el-icon--right"/></el-button>
            <el-button v-if="contents.searchBtnIcon == 0" type="success" @click="search()">{{ contents.searchBtnFont == 1?'鏌ヨ':'' }}</el-button>
          </el-form-item>
        </el-row>

        <el-row class="ad" :style="{justifyContent:contents.btnAdAllBoxPosition=='1'?'flex-start':contents.btnAdAllBoxPosition=='2'?'center':'flex-end'}">
          <el-form-item>
            <el-button
              v-if="isAuth('yonghu','鏂板') && contents.btnAdAllIcon == 1 && contents.btnAdAllIconPosition == 1"
              type="success"
              icon="el-icon-plus"
              @click="addOrUpdateHandler()"
            >{{ contents.btnAdAllFont == 1?'鏂板':'' }}</el-button>
            <el-button
              v-if="isAuth('yonghu','鏂板') && contents.btnAdAllIcon == 1 && contents.btnAdAllIconPosition == 2"
              type="success"
              @click="addOrUpdateHandler()"
            >{{ contents.btnAdAllFont == 1?'鏂板':'' }}<i class="el-icon-plus el-icon--right" /></el-button>
            <el-button
              v-if="isAuth('yonghu','鏂板') && contents.btnAdAllIcon == 0"
              type="success"
              @click="addOrUpdateHandler()"
            >{{ contents.btnAdAllFont == 1?'鏂板':'' }}</el-button>
            <el-button
              v-if="isAuth('yonghu','鍒犻櫎') && contents.btnAdAllIcon == 1 && contents.btnAdAllIconPosition == 1 && contents.tableSelection"
              :disabled="dataListSelections.length <= 0"
              type="danger"
              icon="el-icon-delete"
              @click="deleteHandler()"
            >{{ contents.btnAdAllFont == 1?'鍒犻櫎':'' }}</el-button>
            <el-button
              v-if="isAuth('yonghu','鍒犻櫎') && contents.btnAdAllIcon == 1 && contents.btnAdAllIconPosition == 2 && contents.tableSelection"
              :disabled="dataListSelections.length <= 0"
              type="danger"
              @click="deleteHandler()"
            >{{ contents.btnAdAllFont == 1?'鍒犻櫎':'' }}<i class="el-icon-delete el-icon--right" /></el-button>
            <el-button
              v-if="isAuth('yonghu','鍒犻櫎') && contents.btnAdAllIcon == 0 && contents.tableSelection"
              :disabled="dataListSelections.length <= 0"
              type="danger"
              @click="deleteHandler()"
            >{{ contents.btnAdAllFont == 1?'鍒犻櫎':'' }}</el-button>





          </el-form-item>
        </el-row>
      </el-form>
      <div class="table-content">
        <el-table class="tables" :size="contents.tableSize" :show-header="contents.tableShowHeader"
            :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle"
            :border="contents.tableBorder"
            :fit="contents.tableFit"
            :stripe="contents.tableStripe"
            :style="{width: '100%',fontSize:contents.tableContentFontSize,color:contents.tableContentFontColor}"
            v-if="isAuth('yonghu','鏌ョ湅')"
            :data="dataList"
            v-loading="dataListLoading"
            @selection-change="selectionChangeHandler">
            <el-table-column  v-if="contents.tableSelection"
                type="selection"
                :header-align="contents.tableAlign"
                align="center"
                width="50">
            </el-table-column>
            <el-table-column label="搴忓彿" :align="contents.tableAlign" v-if="contents.tableIndex" type="index" width="50" />
                <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign" 
                    prop="zhanghao"
                   :header-align="contents.tableAlign"
		    label="璐﹀彿">
		     <template #default="scope">
                       {{ getScopeField(scope, 'zhanghao') }}
                     </template>
                </el-table-column>
                <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign" 
                    prop="xingming"
                   :header-align="contents.tableAlign"
		    label="濮撳悕">
		     <template #default="scope">
                       {{ getScopeField(scope, 'xingming') }}
                     </template>
                </el-table-column>
                <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign" 
                    prop="xingbie"
                   :header-align="contents.tableAlign"
		    label="鎬у埆">
		     <template #default="scope">
                       {{ getScopeField(scope, 'xingbie') }}
                     </template>
                </el-table-column>
                <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign" 
                    prop="nianling"
                   :header-align="contents.tableAlign"
		    label="骞撮緞">
		     <template #default="scope">
                       {{ getScopeField(scope, 'nianling') }}
                     </template>
                </el-table-column>
                <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign" 
                    prop="shouji"
                   :header-align="contents.tableAlign"
		    label="鎵嬫満">
		     <template #default="scope">
                       {{ getScopeField(scope, 'shouji') }}
                     </template>
                </el-table-column>
                  <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"  prop="touxiang"
                   :header-align="contents.tableAlign"
                    width="200"
                    label="澶村儚">
                    <template #default="scope">
                      <div v-if="getAvatarUrl(scope)">
                        <img :src="getAvatarUrl(scope)" width="100" height="100">
                      </div>
                      <div v-else>鏆傛棤鍥剧墖</div>
                    </template>
                  </el-table-column>
            <el-table-column width="300" :align="contents.tableAlign" 
               :header-align="contents.tableAlign"
                label="鎿嶄綔">
                <template #default="scope">
                <el-button v-if="isAuth('yonghu','鏌ョ湅') && getScopeRowId(scope) && contents.tableBtnIcon == 1 && contents.tableBtnIconPosition == 1" type="success" icon="el-icon-tickets" size="small" @click="addOrUpdateHandler(getScopeRowId(scope),'info')">{{ contents.tableBtnFont == 1?'璇︽儏':'' }}</el-button>
                <el-button v-if="isAuth('yonghu','鏌ョ湅') && getScopeRowId(scope) && contents.tableBtnIcon == 1 && contents.tableBtnIconPosition == 2" type="success" size="small" @click="addOrUpdateHandler(getScopeRowId(scope),'info')">{{ contents.tableBtnFont == 1?'璇︽儏':'' }}<i class="el-icon-tickets el-icon--right" /></el-button>
                <el-button v-if="isAuth('yonghu','鏌ョ湅') && getScopeRowId(scope) && contents.tableBtnIcon == 0" type="success" size="small" @click="addOrUpdateHandler(getScopeRowId(scope),'info')">{{ contents.tableBtnFont == 1?'璇︽儏':'' }}</el-button>
                <el-button v-if=" isAuth('yonghu','淇敼') && getScopeRowId(scope) && contents.tableBtnIcon == 1 && contents.tableBtnIconPosition == 1" type="primary" icon="el-icon-edit" size="small" @click="addOrUpdateHandler(getScopeRowId(scope))">{{ contents.tableBtnFont == 1?'淇敼':'' }}</el-button>
                <el-button v-if=" isAuth('yonghu','淇敼') && getScopeRowId(scope) && contents.tableBtnIcon == 1 && contents.tableBtnIconPosition == 2" type="primary" size="small" @click="addOrUpdateHandler(getScopeRowId(scope))">{{ contents.tableBtnFont == 1?'淇敼':'' }}<i class="el-icon-edit el-icon--right" /></el-button>
                <el-button v-if=" isAuth('yonghu','淇敼') && getScopeRowId(scope) && contents.tableBtnIcon == 0" type="primary" size="small" @click="addOrUpdateHandler(getScopeRowId(scope))">{{ contents.tableBtnFont == 1?'淇敼':'' }}</el-button>




                <el-button v-if="isAuth('yonghu','鍒犻櫎') && getScopeRowId(scope) && contents.tableBtnIcon == 1 && contents.tableBtnIconPosition == 1" type="danger" icon="el-icon-delete" size="small" @click="deleteHandler(getScopeRowId(scope))">{{ contents.tableBtnFont == 1?'鍒犻櫎':'' }}</el-button>
                <el-button v-if="isAuth('yonghu','鍒犻櫎') && getScopeRowId(scope) && contents.tableBtnIcon == 1 && contents.tableBtnIconPosition == 2" type="danger" size="small" @click="deleteHandler(getScopeRowId(scope))">{{ contents.tableBtnFont == 1?'鍒犻櫎':'' }}<i class="el-icon-delete el-icon--right" /></el-button>
                <el-button v-if="isAuth('yonghu','鍒犻櫎') && getScopeRowId(scope) && contents.tableBtnIcon == 0" type="danger" size="small" @click="deleteHandler(getScopeRowId(scope))">{{ contents.tableBtnFont == 1?'鍒犻櫎':'' }}</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
          clsss="pages"
          :layout="layouts"
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="Number(contents.pageEachNum)"
          :total="totalPage"
          :small="contents.pageStyle"
          class="pagination-content"
          :background="contents.pageBtnBG"
          :style="{textAlign:contents.pagePosition==1?'left':contents.pagePosition==2?'center':'right'}"
        ></el-pagination>
      </div>
    </div>
    <!-- 婵烇綀顕ф慨?濞ｅ浂鍠楅弫鍏笺亜閻㈠憡妗? 閻忓繐妫涢崺妤冪磼閸曨亝顐介柣銊ュearch闁哄倽顫夌涵鑸靛閻樼儵鍋撻幒鏃傝埗閻庢稒鍔楃划宥嗙?-->
    <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>





  </div>
</template>
<script>
import axios from 'axios'
import AddOrUpdate from "./add-or-update";
export default {
  data() {
    return {
      searchForm: {
        key: ""
      },
      form:{},
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      sfshVisiable: false,
      shForm: {},
      chartVisiable: false,
      addOrUpdateFlag:false,
      contents:{"searchBtnFontColor":"rgba(0, 0, 0, 1)","pagePosition":"2","inputFontSize":"14px","inputBorderRadius":"20px","tableBtnDelFontColor":"rgba(0, 0, 0, 1)","tableBtnIconPosition":"1","searchBtnHeight":"42px","tableBgColor":"rgba(255, 255, 255, 1)","inputIconColor":"rgba(0, 0, 0, 1)","searchBtnBorderRadius":"20px","tableStripe":false,"btnAdAllWarnFontColor":"rgba(0, 0, 0, 1)","tableBtnDelBgColor":"rgba(56, 182, 230, 1)","searchBtnIcon":"1","tableSize":"medium","searchBtnBorderStyle":"double","text":{"padding":"0","boxShadow":"0 0 0px rgba(0,0,0,.1)","margin":"0 auto","borderColor":"rgba(0,0,0,.3)","backgroundColor":"rgba(247, 247, 247, 0)","color":"rgba(0, 0, 0, 1)","borderRadius":"0","borderWidth":"0","width":"650px","lineHeight":"50px","fontSize":"26px","borderStyle":"solid"},"tableSelection":true,"searchBtnBorderWidth":"5px 000","tableContentFontSize":"14px","searchBtnBgColor":"#fff","inputTitleSize":"15px","btnAdAllBorderColor":"rgba(56, 182, 230, 1)","pageJumper":true,"btnAdAllIconPosition":"1","searchBoxPosition":"2","tableBtnDetailFontColor":"rgba(0, 0, 0, 1)","tableBtnHeight":"40px","pagePager":true,"searchBtnBorderColor":"rgba(56, 182, 230, 1)","tableHeaderFontColor":"rgba(0, 1, 4, 1)","inputTitle":"1","tableBtnBorderRadius":"5px","btnAdAllFont":"1","btnAdAllDelFontColor":"rgba(0, 0, 0, 1)","tableBtnIcon":"1","btnAdAllHeight":"46px","btnAdAllWarnBgColor":"rgba(255, 255, 255, 1)","btnAdAllBorderWidth":"5px 000","tableStripeFontColor":"rgba(0, 0, 0, 1)","tableBtnBorderStyle":"double","inputHeight":"42px","btnAdAllBorderRadius":"20px","btnAdAllDelBgColor":"rgba(255, 255, 255, 1)","pagePrevNext":true,"btnAdAllAddBgColor":"rgba(255, 255, 255, 1)","searchBtnFont":"1","tableIndex":true,"btnAdAllIcon":"0","tableSortable":false,"pageSizes":true,"tableFit":true,"pageBtnBG":true,"searchBtnFontSize":"15px","tableBtnEditBgColor":"rgba(56, 182, 230, 1)","box":{"padding":"10px 20px","boxShadow":"0 0 6px rgba(0,0,0,0)","flag":1,"backgroundImage":"http://codegen.caihongy.cn/20211124/229bbb98f09a41bc83995f14c422a0a1.jpg","background":"#fff"},"inputBorderWidth":"5px 000","inputFontPosition":"1","inputFontColor":"rgba(0, 0, 0, 1)","pageEachNum":10,"tableHeaderBgColor":"rgba(119, 197, 227, 1)","inputTitleColor":"rgba(0, 0, 0, 1)","btnAdAllBoxPosition":"1","tableBtnDetailBgColor":"rgba(56, 182, 230, 1)","inputIcon":"1","searchBtnIconPosition":"2","btnAdAllFontSize":"14px","inputBorderStyle":"double","tableHoverFontColor":"#333","inputBgColor":"rgba(255, 255, 255, 1)","pageStyle":false,"pageTotal":true,"btnAdAllAddFontColor":"rgba(0, 0, 0, 1)","tableBtnFont":"1","tableContentFontColor":"rgba(0, 0, 0, 1)","inputBorderColor":"rgba(56, 182, 230, 1)","tableShowHeader":true,"tableHoverBgColor":"rgba(119, 197, 227, 0.8)","tableBtnFontSize":"14px","tableBtnBorderColor":"rgba(255, 255, 255, 1)","inputIconPosition":"2","tableBorder":true,"btnAdAllBorderStyle":"double","tableBtnBorderWidth":"5px","tableStripeBgColor":"rgba(119, 197, 227, 0.8)","tableBtnEditFontColor":"rgba(0, 0, 0, 1)","tableAlign":"center"},
      layouts: '',


    };
  },
  created() {
    this.init();
    this.getDataList();
    this.contentStyleChange()
  },
  mounted() {

  },
  filters: {
    htmlfilter: function (val) {
      return val.replace(/<[^>]*>/g).replace(/undefined/g,'');
    }
  },
  components: {
    AddOrUpdate,
  },
  methods: {

    contentStyleChange() {
      this.contentSearchStyleChange()
      this.contentBtnAdAllStyleChange()
      this.contentSearchBtnStyleChange()
      this.contentTableBtnStyleChange()
      this.contentPageStyleChange()
    },
    contentSearchStyleChange() {
      this.$nextTick(()=>{
        document.querySelectorAll('.form-content .slt .el-input__inner').forEach(el=>{
          let textAlign = 'left'
          if(this.contents.inputFontPosition == 2) textAlign = 'center'
          if(this.contents.inputFontPosition == 3) textAlign = 'right'
          el.style.textAlign = textAlign
          el.style.height = this.contents.inputHeight
          el.style.lineHeight = this.contents.inputHeight
          el.style.color = this.contents.inputFontColor
          el.style.fontSize = this.contents.inputFontSize
          el.style.borderWidth = '1px'
          el.style.borderStyle = 'solid'
          el.style.borderColor = '#dcdfe6'
          el.style.borderRadius = '4px'
          el.style.backgroundColor = this.contents.inputBgColor
        })
        if(this.contents.inputTitle) {
          document.querySelectorAll('.form-content .slt .el-form-item__label').forEach(el=>{
            el.style.color = this.contents.inputTitleColor
            el.style.fontSize = this.contents.inputTitleSize
            el.style.lineHeight = this.contents.inputHeight
          })
        }
        setTimeout(()=>{
          document.querySelectorAll('.form-content .slt .el-input__prefix').forEach(el=>{
            el.style.color = this.contents.inputIconColor
            el.style.lineHeight = this.contents.inputHeight
          })
          document.querySelectorAll('.form-content .slt .el-input__suffix').forEach(el=>{
            el.style.color = this.contents.inputIconColor
            el.style.lineHeight = this.contents.inputHeight
          })
          document.querySelectorAll('.form-content .slt .el-input__icon').forEach(el=>{
            el.style.lineHeight = this.contents.inputHeight
          })
        },10)

      })
    },
    // 闁瑰吋绮庨崒銊╁箰婢舵劖灏?
    contentSearchBtnStyleChange() {
      this.$nextTick(()=>{
        document.querySelectorAll('.form-content .slt .el-button--success').forEach(el=>{
          el.style.height = this.contents.searchBtnHeight
          el.style.color = this.contents.searchBtnFontColor
          el.style.fontSize = this.contents.searchBtnFontSize
          el.style.borderWidth = this.contents.searchBtnBorderWidth
          el.style.borderStyle = this.contents.searchBtnBorderStyle
          el.style.borderColor = this.contents.searchBtnBorderColor
          el.style.borderRadius = this.contents.searchBtnBorderRadius
          el.style.backgroundColor = this.contents.searchBtnBgColor
        })
      })
    },
    // 闁哄倹婢橀·鍐Υ娴ｇ懓顥楅梺鎻掔箰閸ㄥ綊姊?
    contentBtnAdAllStyleChange() {
      this.$nextTick(()=>{
        document.querySelectorAll('.form-content .ad .el-button--success').forEach(el=>{
          el.style.height = this.contents.btnAdAllHeight
          el.style.color = this.contents.btnAdAllAddFontColor
          el.style.fontSize = this.contents.btnAdAllFontSize
          el.style.borderWidth = this.contents.btnAdAllBorderWidth
          el.style.borderStyle = this.contents.btnAdAllBorderStyle
          el.style.borderColor = this.contents.btnAdAllBorderColor
          el.style.borderRadius = this.contents.btnAdAllBorderRadius
          el.style.backgroundColor = this.contents.btnAdAllAddBgColor
        })
        document.querySelectorAll('.form-content .ad .el-button--danger').forEach(el=>{
          el.style.height = this.contents.btnAdAllHeight
          el.style.color = this.contents.btnAdAllDelFontColor
          el.style.fontSize = this.contents.btnAdAllFontSize
          el.style.borderWidth = this.contents.btnAdAllBorderWidth
          el.style.borderStyle = this.contents.btnAdAllBorderStyle
          el.style.borderColor = this.contents.btnAdAllBorderColor
          el.style.borderRadius = this.contents.btnAdAllBorderRadius
          el.style.backgroundColor = this.contents.btnAdAllDelBgColor
        })
        document.querySelectorAll('.form-content .ad .el-button--warning').forEach(el=>{
          el.style.height = this.contents.btnAdAllHeight
          el.style.color = this.contents.btnAdAllWarnFontColor
          el.style.fontSize = this.contents.btnAdAllFontSize
          el.style.borderWidth = this.contents.btnAdAllBorderWidth
          el.style.borderStyle = this.contents.btnAdAllBorderStyle
          el.style.borderColor = this.contents.btnAdAllBorderColor
          el.style.borderRadius = this.contents.btnAdAllBorderRadius
          el.style.backgroundColor = this.contents.btnAdAllWarnBgColor
        })
      })
    },
    // 閻炴稏鍔嶉悧?
    // rowStyle({ row, rowIndex}) {
    //   if (rowIndex % 2 == 1) {
    //     if(this.contents.tableStripe) {
    //       return {color:this.contents.tableStripeFontColor}
    //     }
    //   } else {
    //     return ''
    //   }
    // },
    // cellStyle({ row, rowIndex}){
    //   if (rowIndex % 2 == 1) {
    //     if(this.contents.tableStripe) {
    //       return {backgroundColor:this.contents.tableStripeBgColor}
    //     }
    //   } else {
    //     return ''
    //   }
    // },
    headerRowStyle({ row, rowIndex}){
      return {color: this.contents.tableHeaderFontColor}
    },
    headerCellStyle({ row, rowIndex}){
      return {backgroundColor: this.contents.tableHeaderBgColor}
    },
    // 閻炴稏鍔嶉悧鎼佸箰婢舵劖灏?
    contentTableBtnStyleChange(){
      // this.$nextTick(()=>{
      //   setTimeout(()=>{
      //     document.querySelectorAll('.table-content .tables .el-table__body .el-button--success').forEach(el=>{
      //       el.style.height = this.contents.tableBtnHeight
      //       el.style.color = this.contents.tableBtnDetailFontColor
      //       el.style.fontSize = this.contents.tableBtnFontSize
      //       el.style.borderWidth = this.contents.tableBtnBorderWidth
      //       el.style.borderStyle = this.contents.tableBtnBorderStyle
      //       el.style.borderColor = this.contents.tableBtnBorderColor
      //       el.style.borderRadius = this.contents.tableBtnBorderRadius
      //       el.style.backgroundColor = this.contents.tableBtnDetailBgColor
      //     })
      //     document.querySelectorAll('.table-content .tables .el-table__body .el-button--primary').forEach(el=>{
      //       el.style.height = this.contents.tableBtnHeight
      //       el.style.color = this.contents.tableBtnEditFontColor
      //       el.style.fontSize = this.contents.tableBtnFontSize
      //       el.style.borderWidth = this.contents.tableBtnBorderWidth
      //       el.style.borderStyle = this.contents.tableBtnBorderStyle
      //       el.style.borderColor = this.contents.tableBtnBorderColor
      //       el.style.borderRadius = this.contents.tableBtnBorderRadius
      //       el.style.backgroundColor = this.contents.tableBtnEditBgColor
      //     })
      //     document.querySelectorAll('.table-content .tables .el-table__body .el-button--danger').forEach(el=>{
      //       el.style.height = this.contents.tableBtnHeight
      //       el.style.color = this.contents.tableBtnDelFontColor
      //       el.style.fontSize = this.contents.tableBtnFontSize
      //       el.style.borderWidth = this.contents.tableBtnBorderWidth
      //       el.style.borderStyle = this.contents.tableBtnBorderStyle
      //       el.style.borderColor = this.contents.tableBtnBorderColor
      //       el.style.borderRadius = this.contents.tableBtnBorderRadius
      //       el.style.backgroundColor = this.contents.tableBtnDelBgColor
      //     })

      //   }, 50)
      // })
    },
    // 闁告帒妫濋妴?
    contentPageStyleChange(){
      let arr = []

      if(this.contents.pageTotal) arr.push('total')
      if(this.contents.pageSizes) arr.push('sizes')
      if(this.contents.pagePrevNext){
        arr.push('prev')
        if(this.contents.pagePager) arr.push('pager')
        arr.push('next')
      }
      if(this.contents.pageJumper) arr.push('jumper')
      this.layouts = arr.join()
      this.contents.pageEachNum = 10
    },

    init () {
    },
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    getScopeRow(scope) {
      return scope && scope.row ? scope.row : null;
    },
    getScopeField(scope, field) {
      const row = this.getScopeRow(scope);
      return row && row[field] !== undefined && row[field] !== null ? row[field] : '';
    },
    getScopeRowId(scope) {
      const row = this.getScopeRow(scope);
      return row && row.id ? row.id : '';
    },
    getAvatarUrl(scope) {
      const touxiang = this.getScopeField(scope, 'touxiang');
      return touxiang ? this.$base.url + String(touxiang).split(',')[0] : '';
    },

    // 闁兼儳鍢茶ぐ鍥极閻楀牆绁﹂柛鎺擃殙閵?
    getDataList() {
      this.dataListLoading = true;
      let params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
      }
          if(this.searchForm.zhanghao!='' && this.searchForm.zhanghao!=undefined){
            params['zhanghao'] = '%' + this.searchForm.zhanghao + '%'
          }
          if(this.searchForm.xingming!='' && this.searchForm.xingming!=undefined){
            params['xingming'] = '%' + this.searchForm.xingming + '%'
          }
      this.$http({
        url: "yonghu/page",
        method: "get",
        params: params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list;
          this.totalPage = data.data.total;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      }).catch(() => {
        this.dataList = [];
        this.totalPage = 0;
        this.dataListLoading = false;
      });
    },
    // 婵絽绻橀妴澶愬极?
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 鐟滅増鎸告晶鐘炽亜?
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 濠㈣埖宀搁埀?
    selectionChangeHandler(val) {
      this.dataListSelections = val;
    },
    // 婵烇綀顕ф慨?濞ｅ浂鍠楅弫?
    addOrUpdateHandler(id,type) {
      this.showFlag = false;
      this.addOrUpdateFlag = true;
      this.crossAddOrUpdateFlag = false;
      if(type!='info'){
        type = 'else';
      }
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id,type);
      });
    },
    // 闁哄被鍎冲﹢鍛嫚閸曨噮鍟?
    // 濞戞挸顑堝ù?
    download(file){
      window.open(`${file}`)
    },
    // 闁告帞濞€濞?
    deleteHandler(id) {
      var ids = id
        ? [Number(id)]
        : this.dataListSelections.map(item => {
            return Number(item.id);
          });
      this.$confirm(`是否确认${id ? '删除' : '批量删除'}选中的用户记录？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http({
          url: "yonghu/delete",
          method: "post",
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "删除成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.search();
              }
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },


  }

};
</script>
<style lang="scss" scoped>
  .slt {
    margin: 0 !important;
    display: flex;
  }

  .ad {
    margin: 0 !important;
    display: flex;
  }

  .pages {
    :deep(.el-pagination__sizes) {
      :deep(.el-input__inner) {
        height: 22px;
        line-height: 22px;
      }
    }
  }
  

  .el-button+.el-button {
    margin:0;
  } 

  .tables {
	:deep(.el-button--success) {
		height: 40px;
		color: rgba(0, 0, 0, 1);
		font-size: 14px;
		border-width: 5px;
		border-style: double;
		border-color: rgba(255, 255, 255, 1);
		border-radius: 5px;
		background-color: rgba(56, 182, 230, 1);
	}
	
	:deep(.el-button--primary) {
		height: 40px;
		color: rgba(0, 0, 0, 1);
		font-size: 14px;
		border-width: 5px;
		border-style: double;
		border-color: rgba(255, 255, 255, 1);
		border-radius: 5px;
		background-color: rgba(56, 182, 230, 1);
	}
	
	:deep(.el-button--danger) {
		height: 40px;
		color: rgba(0, 0, 0, 1);
		font-size: 14px;
		border-width: 5px;
		border-style: double;
		border-color: rgba(255, 255, 255, 1);
		border-radius: 5px;
		background-color: rgba(56, 182, 230, 1);
	}

    :deep(.el-button) {
      margin: 4px;
    }
  }
	.form-content {
		background: transparent;
	}
	.table-content {
		background: transparent;
	}
	
	:deep(.el-table__body tr) {
				background-color: rgba(255, 255, 255, 1) !important;
				color: rgba(0, 0, 0, 1) !important;
	 }
	:deep(.el-table__body tr.el-table__row--striped td) {
	    background: transparent;
	}
	:deep(.el-table__body tr.el-table__row--striped) {
		background-color: rgba(119, 197, 227, 0.8) !important;
		color: rgba(0, 0, 0, 1) !important;
	}
	
	 :deep(.el-table__body tr:hover>td) {
	   	   background-color: rgba(119, 197, 227, 0.8) !important;
	   	   	   color: #333 !important;
	   	 }
	 
</style>



