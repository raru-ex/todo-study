export interface Todo extends UnstoredTodo {
  id: number
}

export interface UnstoredTodo {
  name: string
  content: string
}
