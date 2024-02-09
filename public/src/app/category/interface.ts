export interface Category {
    name:string
    id: number
    ownerId: number
}

export interface CategoryResponse {
    category: Category[]
    total: number
}