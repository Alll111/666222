<template>
  <div class="editor-wrap">
    <el-upload
      ref="uploadRef"
      class="avatar-uploader"
      :action="getActionUrl"
      name="file"
      :headers="header"
      :show-file-list="false"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      :before-upload="beforeUpload"
    ></el-upload>

    <QuillEditor
      class="editor"
      ref="myQuillEditor"
      theme="snow"
      contentType="html"
      :content="content"
      :options="editorOption"
      @ready="onEditorReady"
      @blur="onEditorBlur"
      @focus="onEditorFocus"
      @update:content="onEditorChange"
    />
  </div>
</template>
<script>
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";

const toolbarOptions = [
  ["bold", "italic", "underline", "strike"],
  ["blockquote", "code-block"],
  [{ header: 1 }, { header: 2 }],
  [{ list: "ordered" }, { list: "bullet" }],
  [{ script: "sub" }, { script: "super" }],
  [{ indent: "-1" }, { indent: "+1" }],
  [{ size: ["small", false, "large", "huge"] }],
  [{ header: [1, 2, 3, 4, 5, 6, false] }],
  [{ color: [] }, { background: [] }],
  [{ font: [] }],
  [{ align: [] }],
  ["clean"],
  ["link", "image", "video"]
];

export default {
  props: {
    modelValue: {
      type: String,
      default: ""
    },
    value: {
      type: String
    },
    action: {
      type: String
    },
    maxSize: {
      type: Number,
      default: 4000
    }
  },

  components: {
    QuillEditor
  },

  data() {
    return {
      content: this.modelValue || this.value || "",
      quill: null,
      quillUpdateImg: false,
      editorOption: {
        placeholder: "",
        theme: "snow",
        modules: {
          toolbar: {
            container: toolbarOptions,
            handlers: {
              image: () => this.triggerImageUpload()
            }
          }
        }
      },
      header: {
       'Token': this.$storage.get("Token")
      }
    };
  },
  computed: {
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
    triggerImageUpload() {
      const uploadEl = this.$refs.uploadRef && this.$refs.uploadRef.$el
      const input = uploadEl ? uploadEl.querySelector("input[type='file']") : null
      if (input) {
        input.click()
      }
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
    onEditorBlur() {},
    onEditorFocus() {},
    onEditorReady(quill) {
      this.quill = quill
    },
    onEditorChange(value) {
      this.content = value || ""
      this.$emit("input", this.content);
      this.$emit("update:modelValue", this.content);
    },
    beforeUpload(file) {
      const isLtMax = file.size / 1024 <= this.maxSize
      if (!isLtMax) {
        this.$message.error(`上传图片大小不能超过 ${this.maxSize}KB`)
        return false
      }
      this.quillUpdateImg = true;
      return true
    },

    uploadSuccess(res) {
      const quill = this.quill;
      if (res && res.code === 0 && quill) {
        const selection = quill.getSelection(true)
        const length = selection ? selection.index : quill.getLength()
        quill.insertEmbed(length, "image", this.buildAccessUrl(this.resolveUploadPath(res)));
        quill.setSelection(length + 1);
      } else {
        this.$message.error("图片插入失败");
      }
      this.quillUpdateImg = false;
    },
    uploadError() {
      this.quillUpdateImg = false;
      this.$message.error("图片插入失败");
    }
  }
  ,
  watch: {
    value(v) {
      if (v !== this.content) {
        this.content = v || ""
      }
    },
    modelValue(v) {
      if (v !== this.content) {
        this.content = v || ""
      }
    }
  }
};
</script>

<style>
.editor-wrap {
  width: 100%;
}

.avatar-uploader {
  display: none;
}

.editor {
  line-height: normal !important;
}
.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}
.ql-container {
	height: 400px;
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "标题1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "标题2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "标题3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "标题4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "标题5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}
</style>
