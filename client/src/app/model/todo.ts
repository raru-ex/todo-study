export type Todo = Todo.NoId & {
  id: Todo.Id
};

export module Todo {
  export type     Id = Identity<'Todo'> & number;
  export function Id(id: number): Id { return id as Id; }

  export interface NoId {
    name: string;
    content: string;
  }
}
