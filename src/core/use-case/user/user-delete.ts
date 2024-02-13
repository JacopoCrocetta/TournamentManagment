import { Inject, Injectable } from "@nestjs/common";
import { User } from "src/core/entity/abstract-user";
import { IUserRepository } from "src/core/repository/user.repository";


@Injectable()
export class DeleteUser {
  constructor(@Inject() userRepository: IUserRepository) {}

  delete = async (input: User) => {
    return null;
  };
}
