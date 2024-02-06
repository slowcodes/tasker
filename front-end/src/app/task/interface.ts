import { Category } from "../category/interface"

export interface Task {
    id: number
    name:string
    description: string
    category: Category
    ownerId: string
    due_date: Date
    priority: string
}



export interface TaskResponse {
    total:number
    tasks: Task[]
}