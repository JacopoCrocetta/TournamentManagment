import { Module } from "@nestjs/common";
import { UserCreate } from "./user-create";
import { DeleteUser } from "./user-delete";
import { UserPage } from "./user-page";
import { UpdateUser } from "./user-update";

const providers = [UserCreate, DeleteUser, UserPage, UpdateUser];

@Module({ providers, exports: [...providers] })
export class UserModule{}