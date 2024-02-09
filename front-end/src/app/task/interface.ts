import { Category } from "../category/interface"

export interface Task {
    id: number
    name:string
    description: string
    category: Category
    ownerId: string
    due_date: Date
    priority: string
    status: string
}



export interface TaskResponse {
    total:number
    tasks: Task[]
}

export interface TaskRequestParams {
    priority: string,
    status: string,
    due_date: Date,
    search_text:string,
    isSearch:number,
    isFilter:number
}