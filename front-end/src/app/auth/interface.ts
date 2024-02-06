export interface SignUp {
    email:string
    password:string
    password_confirm:string
    first_name:string
    last_name:string
    role: string
}

export interface Login {
    email:string
    password:number
}

export interface AccessToken {
    accessToken:string
}