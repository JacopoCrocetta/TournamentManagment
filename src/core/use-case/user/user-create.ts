import { Inject, Injectable } from "@nestjs/common";
import { User } from "src/core/entity/abstract-user";
import { IUserRepository } from "src/core/repository/user.repository";

@Injectable()
export class UserCreate {
  constructor(@Inject() userRepository: IUserRepository) {}

  create = async (input: User) => {
    return null;
  };
}
