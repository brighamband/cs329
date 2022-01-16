# Dafny Docker Container
# [Choice] Ubuntu version (use hirsuite or bionic on local arm64/Apple Silicon): hirsute, focal, bionic
ARG VARIANT=focal
FROM mcr.microsoft.com/vscode/devcontainers/base:${VARIANT}

# Allow students to install what they want in the container terminal
RUN apt-get update && apt-get install -y sudo \
    && echo vscode ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/vscode \
    && chmod 0440 /etc/sudoers.d/vscode

# Use this section to install additional OS packages.
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
        # Dependencies to install Dafny and dotnet
        wget unzip \
        # Z3 dependency
        libgomp1

USER vscode
WORKDIR /home/vscode

# install dotnet manually
RUN wget -nv https://dot.net/v1/dotnet-install.sh \
    && chmod 755 dotnet-install.sh \
    && ./dotnet-install.sh -c 5.0 --runtime aspnetcore
ENV PATH="/home/vscode/.dotnet:${PATH}"

# Install dafny command line interface
RUN wget -nv https://github.com/dafny-lang/dafny/releases/download/v3.3.0/dafny-3.3.0-x64-ubuntu-16.04.zip \
    && unzip dafny-3.3.0-x64-ubuntu-16.04.zip \
    && rm -rf dafny-3.3.0-x64-ubuntu-16.04.zip
ENV PATH="/home/vscode/dafny:${PATH}"
