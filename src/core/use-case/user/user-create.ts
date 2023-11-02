import { Inject, Injectable } from "@nestjs/common";
import { IUserRepository } from "src/core/repository/user.repository";

interface CreateUserInput {}

@Injectable()
export class UserCreate {
  constructor(@Inject() userRepository: IUserRepository) {}

  create = async (input: CreateUserInput) => {
    return null;
  };
}
