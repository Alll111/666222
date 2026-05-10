<template>
  <div>
    <!-- 上传文件组件 -->
    <el-upload
      ref="upload"
      :action="getActionUrl"
      list-type="picture-card"
      :multiple="multiple"
      :limit="limit"
      :headers="myHeaders"
      :file-list="fileList"
      :on-exceed="handleExceed"
      :on-preview="handleUploadPreview"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadErr"
      :before-upload="handleBeforeUpload"
    >
      <el-icon><Plus /></el-icon>
      <template #tip>
        <div class="el-upload__tip" style="color:#838fa1;">{{tip}}</div>
      </template>
    </el-upload>
    <el-dialog v-model="dialogVisible" width="800px" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt>
    </el-dialog>
  </div>
</template>
<script>
import storage from "@/utils/storage";
import { Plus } from '@element-plus/icons-vue'
export default {
  components: { Plus },
  emits: ["change"],
  data() {
    return {
      // 查看大图
      dialogVisible: false,
      // 查看大图
      dialogImageUrl: "",
      // 组件渲染图片使用的文件列表
      fileList: [],
      fileUrlList: [],
      myHeaders:{},
      isUnmounted: false
    };
  },
  props: {
    tip: {
      type: String,
      default: ""
    },
    action: {
      type: String,
      default: ""
    },
    limit: {
      type: Number,
      default: 1
    },
    multiple: {
      type: Boolean,
      default: false
    },
    fileUrls: {
      type: [String, Array, Object, null],
      default: ""
    }
  },
  mounted() {
    this.init();
    this.myHeaders= {
      'Token':storage.get("Token")
    }
  },
  beforeUnmount() {
    this.isUnmounted = true;
    this.dialogVisible = false;
    this.dialogImageUrl = "";
    const upload = this.$refs.upload;
    upload?.abort?.();
    upload?.clearFiles?.();
    this.fileList = [];
    this.fileUrlList = [];
  },
  watch: {
    fileUrls: function(val, oldVal) {
      //   console.log("new: %s, old: %s", val, oldVal);
      if (this.isUnmounted) {
        return;
      }
      this.init();
    }
  },
  computed: {
    // 计算属性的 getter
    getActionUrl: function() {
      const action = String(this.action || "").trim();
      if (!action) {
        return this.$base.url;
      }
      if (/^https?:\/\//.test(action) || action.startsWith("/api/")) {
        return action;
      }
      return `${this.$base.url.replace(/\/+$/, "")}/${action.replace(/^\/+/, "")}`;
    }
  },
  methods: {
    normalizeFileUrls(value) {
      if (typeof value === "string") {
        return value.trim();
      }
      if (Array.isArray(value)) {
        return value
          .map(item => this.normalizeStoredUrl(item && item.url ? item.url : item))
          .filter(Boolean)
          .join(",");
      }
      if (value && typeof value === "object") {
        if (typeof value.url === "string") {
          return this.normalizeStoredUrl(value.url);
        }
        return "";
      }
      return "";
    },
    normalizeStoredUrl(url) {
      let normalized = String(url || "").split("?")[0].trim();
      if (!normalized) {
        return "";
      }
      normalized = normalized.replace(/^https?:\/\/[^/]+/i, "");
      normalized = normalized.replace(/^\/+api(?=\/)/i, "");
      normalized = normalized.replace(/^\/+/, "");
      if (normalized.startsWith("upload/")) {
        return normalized;
      }
      return `upload/${normalized}`;
    },
    buildAccessUrl(url) {
      const normalized = this.normalizeStoredUrl(url);
      if (!normalized) {
        return "";
      }
      return `${this.$base.url.replace(/\/+$/, "")}/${normalized}`;
    },
    resolveUploadPath(res) {
      if (!res) {
        return "";
      }
      const raw = res.url || res.file || res.path || "";
      if (!raw) {
        return "";
      }
      if (/^https?:\/\//.test(raw) || raw.startsWith("/upload/") || raw.startsWith("upload/")) {
        return this.normalizeStoredUrl(raw);
      }
      return this.normalizeStoredUrl(`upload/${raw}`);
    },
    // 初始化文件列表
    init() {
      if (this.isUnmounted) {
        return;
      }
      const normalizedFileUrls = this.normalizeFileUrls(this.fileUrls);
      if (normalizedFileUrls) {
        this.fileUrlList = normalizedFileUrls.split(",").filter(Boolean);
        let fileArray = [];
        this.fileUrlList.forEach(function(item, index) {
          var url = item;
          var name = index;
          var file = {
            name: name,
            url: url
          };
          fileArray.push(file);
        });
        this.setFileList(fileArray);
      } else {
        this.fileList = [];
        this.fileUrlList = [];
      }
    },
    handleBeforeUpload(file) {
      if (this.isUnmounted) {
        return false;
      }
    },
    // 上传文件成功后执行
    handleUploadSuccess(res, file, fileList) {
      if (this.isUnmounted) {
        return;
      }
      if (!Array.isArray(fileList) || fileList.length === 0) {
        return;
      }
      if (res && res.code === 0) {
        fileList[fileList.length - 1]["url"] = this.resolveUploadPath(res);
        this.setFileList(fileList);
        if (this.isUnmounted) {
          return;
        }
        this.$emit("change", this.fileUrlList.join(","));
      } else {
        this.$message.error(res.msg);
      }
    },
    // 图片上传失败
    handleUploadErr(err, file, fileList) {
      if (this.isUnmounted) {
        return;
      }
      this.$message.error("文件上传失败");
    },
    // 移除图片
    handleRemove(file, fileList) {
      if (this.isUnmounted) {
        return;
      }
      this.setFileList(fileList);
      if (this.isUnmounted) {
        return;
      }
      this.$emit("change", this.fileUrlList.join(","));
    },
    // 查看大图
    handleUploadPreview(file) {
      if (this.isUnmounted || !file) {
        return;
      }
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 限制图片数量
    handleExceed(files, fileList) {
      this.$message.warning(`最多上传${this.limit}张图片`);
    },
    // 重新同步 fileList 和 fileUrlList
    setFileList(fileList) {
      if (this.isUnmounted) {
        return;
      }
      var fileArray = [];
      var fileUrlArray = [];
      let _this = this;
      (Array.isArray(fileList) ? fileList : []).forEach(function(item, index) {
        if (!item || !item.url) {
          return;
        }
        var storedUrl = _this.normalizeStoredUrl(item.url);
        if (!storedUrl) {
          return;
        }
        var url = _this.buildAccessUrl(storedUrl);
        var name = item.name;
        var file = {
          name: name,
          url: url
        };
        fileArray.push(file);
        fileUrlArray.push(storedUrl);
      });
      this.fileList = fileArray;
      this.fileUrlList = fileUrlArray;
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
