import { h } from 'vue'
import { ElIcon } from 'element-plus'
import * as EpIcons from '@element-plus/icons-vue'

const explicitMap = {
  'el-icon-search': 'Search',
  'el-icon-plus': 'Plus',
  'el-icon-delete': 'Delete',
  'el-icon-edit': 'Edit',
  'el-icon-time': 'Timer',
  'el-icon-back': 'Back',
  'el-icon-menu': 'Menu',
  'el-icon-message': 'Message',
  'el-icon-postcard': 'Postcard',
  'el-icon-position': 'Position',
  'el-icon-stopwatch': 'Stopwatch',
  'el-icon-copy-document': 'CopyDocument',
  'el-icon-chat-dot-round': 'ChatDotRound',
  'el-icon-chat-dot-square': 'ChatDotSquare',
  'el-icon-close-notification': 'Bell',
  'el-icon-crop': 'Crop',
  'el-icon-full-screen': 'FullScreen',
  'el-icon-switch-button': 'SwitchButton',
  'el-icon-user-solid': 'UserFilled',
  'el-icon-tickets': 'Tickets',
  'el-icon-odometer': 'Odometer',
  'el-icon-mic': 'Microphone',
  'el-icon-microphone': 'Microphone',
  'el-icon-aim': 'Aim',
  'el-icon-s-home': 'House',
  'el-icon-s-grid': 'Grid',
  'el-icon-s-management': 'Management',
  'el-icon-s-marketing': 'Promotion',
  'el-icon-s-shop': 'Shop',
  'el-icon-s-ticket': 'Ticket',
  'el-icon-s-fold': 'Fold',
  'el-icon-s-unfold': 'Expand',
  'el-icon-s-operation': 'Operation',
  'el-icon-s-opportunity': 'Opportunity',
  'el-icon-s-data': 'DataAnalysis',
  'el-icon-s-flag': 'Flag',
  'el-icon-s-order': 'List',
  'el-icon-s-platform': 'Platform',
  'el-icon-s-promotion': 'Promotion',
  'el-icon-s-release': 'UploadFilled',
  'el-icon-s-comment': 'Comment',
  'el-icon-s-claim': 'Checked',
  'el-icon-s-open': 'FolderOpened',
  'el-icon-s-custom': 'User',
  'el-icon-s-cooperation': 'Connection',
  'el-icon-s-finance': 'Money',
  'el-icon-bangzhu': 'QuestionFilled'
}

function registerLegacyIcon(app, legacyName, iconCompName) {
  const IconComp = EpIcons[iconCompName] || EpIcons.InfoFilled
  app.component(legacyName, {
    name: legacyName,
    render() {
      return h(ElIcon, null, { default: () => h(IconComp) })
    }
  })
}

export function registerLegacyElIcons(app) {
  Object.entries(explicitMap).forEach(([legacyName, iconCompName]) => {
    registerLegacyIcon(app, legacyName, iconCompName)
  })
}

