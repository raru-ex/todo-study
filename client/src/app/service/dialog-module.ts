export module Dialog {
  /**
   * ダイアログの種類を管理するEnum
   */
  export enum DialogType {
    CONFIRM,
    INFO,
    WARNING,
    ERROR
  }

  export type DialogServiceData = {
    title: string,
    content: string,
    type: DialogType
  };
}
