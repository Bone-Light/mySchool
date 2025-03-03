import {defineStore} from "pinia";

export const useStore = defineStore('general', {
  state: () => {
      return {
          username: '',
          email: '',
          role: '',
          registerTime: null
      }
  }
})